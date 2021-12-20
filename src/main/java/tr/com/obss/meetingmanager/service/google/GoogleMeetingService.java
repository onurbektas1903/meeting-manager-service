package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.clients.GoogleRestClient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapper;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.common.properties.ApplicationKafkaTopics;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleMeetingService implements MeetingService {
    private final ApplicationKafkaTopics topics;
    private final GoogleCalendarServiceClient calendarClientService;
    private final GoogleMapper googleMapper;
    private final GoogleRestClient googleRestClient;

    @Override
    @Transactional("ptm")
    public MeetingDTO handleCreate(MeetingDTO meetingDTO) {
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,true);
        CalendarEventDTO response = googleRestClient.scheduleEvent(calendarEvent).getBody();
        meetingDTO.setCalendarEventId(response.getEventId());
        meetingDTO.setMeetingURL(response.getMeetingUrl());
        return meetingDTO;
    }

    @Transactional("ptm")
    public void addMeetingToCalendar(MeetingDTO meetingDTO){
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,false);
        CalendarEventDTO response = googleRestClient.scheduleEvent(calendarEvent).getBody();
        meetingDTO.setCalendarEventId(response.getEventId());
    }


    @Transactional("ptm")
    public void sendChangeSlotMail(SlotRequestDTO slotRequestDTO){
        calendarClientService.changeSlot(googleMapper.toGoogleMailDTO(slotRequestDTO));
    }
    @Override
    @Transactional("ptm")
    public MeetingDTO handleUpdate(MeetingDTO meetingDTO) {
        updateCalendarMeeting(meetingDTO,true);
        return meetingDTO;
    }
    @Transactional("ptm")
    public void updateCalendarMeeting(MeetingDTO meetingDTO,boolean withMeet){
        calendarClientService.updateEvent(googleMapper.toCalendarEventDTO(meetingDTO,withMeet));
    }

    @Override
    @Transactional("ptm")
    public void handleCancel(MeetingDTO meetingDTO) {
        calendarClientService.deleteEvent(new DeleteEventDTO(meetingDTO.getProviderAccount(),
                        meetingDTO.getOrganizer(),meetingDTO.getCalendarEventId()));
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return GOOGLE;
    }

    @Override
    public void handleRollback(MeetingDTO meetingDTO) {

    }

}
