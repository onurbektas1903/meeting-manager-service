package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapperDecorator;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.MeetingService;

import javax.transaction.Transactional;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleMeetingService implements MeetingService {
    @Value("${topics.notification}")
    private String notificationTopic;

    private final GoogleCalendarServiceClient calendarClientService;
    private final GoogleAccountService googleAccountService;
    private final GoogleMapperDecorator googleMapper;
    private final KafkaMessageSender messageSender;

    @Override
    @Transactional
    public MeetingDTO handleCreate(MeetingDTO meetingDTO) {
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleAccount,true);
        CalendarEventDTO response = calendarClientService.scheduleMeeting(calendarEvent);
        meetingDTO.setEventId(response.getEventId());
        meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(googleAccount.getId()).build());
        return meetingDTO;
    }
    @Transactional
    public void addMeetingToCalendar(MeetingDTO meetingDTO){
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleAccount,false);
        CalendarEventDTO response = calendarClientService.scheduleMeeting(calendarEvent);
        meetingDTO.setEventId(response.getEventId());
    }
    @Transactional
    public void sendChangeSlotMail(SlotRequestDTO slotRequestDTO){
        calendarClientService.changeSlot(googleMapper.toGoogleMailDTO(slotRequestDTO,
                googleAccountService.findActiveAccount()));
    }
    @Override
    @Transactional
    public MeetingDTO handleUpdate(MeetingDTO meetingDTO) {
        updateCalendarMeeting(meetingDTO,true);
        return meetingDTO;
    }
    @Transactional
    public void updateCalendarMeeting(MeetingDTO meetingDTO,boolean withMeet){
        calendarClientService.updateMeeting(googleMapper.toCalendarEventDTO(meetingDTO,
                googleAccountService.findActiveAccount(),withMeet));
    }

    @Override
    public MeetingDTO handleCancel(MeetingDTO meetingDTO) {
        return null;
    }

    @Override
    public List<MeetingDTO> listMeetings() {
        return null;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return GOOGLE;
    }

}
