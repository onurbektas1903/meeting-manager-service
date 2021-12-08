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

    private final MeetingManagerService meetingManagerService;
    private final GoogleCalendarServiceClient calendarClientService;
    private final GoogleAccountService googleAccountService;
    private final GoogleMapperDecorator googleMapper;
    private final KafkaMessageSender messageSender;

    @Override
    @Transactional
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleAccount,true);
        CalendarEventDTO response = calendarClientService.scheduleMeeting(calendarEvent);
        meetingDTO.setEventId(response.getEventId());
        meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(googleAccount.getId()).build());
        return meetingManagerService.saveMeeting(meetingDTO);
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
    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
//        calendarClientService.updateMeeting(googleMapper.toCalendarEventDTO(meetingDTO,
//                googleAccountService.findActiveAccount(),true));
        return meetingManagerService.updateMeeting(meetingDTO);
    }
    public void updateCalendarMeeting(MeetingDTO meetingDTO){
        calendarClientService.updateMeeting(googleMapper.toCalendarEventDTO(meetingDTO,
                googleAccountService.findActiveAccount(),false));
    }

    @Override
    public MeetingDTO cancelMeeting(MeetingDTO meetingDTO) {
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

    @Override
    @Transactional
    public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
//        meetingManagerService
        return null;
    }
}
