package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapperDecorator;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.common.properties.ApplicationKafkaTopics;

import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleMeetingService implements MeetingService {
    private final ApplicationKafkaTopics topics;
    private final GoogleCalendarServiceClient calendarClientService;
    private final GoogleAccountService googleAccountService;
    private final GoogleMapperDecorator googleMapper;
    private final KafkaMessageSender messageSender;

    @Override
//    @Transactional("dstm")
    @Transactional
    public MeetingDTO handleCreate(MeetingDTO meetingDTO) {
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleAccount,true);
        CalendarEventDTO response = calendarClientService.scheduleEvent(calendarEvent);
        meetingDTO.setMeetingURL(response.getMeetingUrl());
        meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(googleAccount.getId()).build());
        return meetingDTO;
    }
//    @Transactional("dstm")
    public void addMeetingToCalendar(MeetingDTO meetingDTO){
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleAccount,false);
        messageSender.sendCreated(topics.getGoogle(),calendarEvent);
    }
//    @Transactional("dstm")
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
        calendarClientService.updateEvent(googleMapper.toCalendarEventDTO(meetingDTO,
                googleAccountService.findActiveAccount(),withMeet));
    }

    @Override
    public void handleCancel(MeetingDTO meetingDTO) {
        GoogleAccountDTO googleAccount = googleAccountService.findActiveAccount();
        DeleteEventDTO deleteEventDTO = googleMapper.toDeleteEventDTO(googleAccount,meetingDTO);
        calendarClientService.deleteEvent(deleteEventDTO,meetingDTO.getCalendarEventId());
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
