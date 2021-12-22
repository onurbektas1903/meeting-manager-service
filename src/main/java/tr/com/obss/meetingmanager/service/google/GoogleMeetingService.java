package tr.com.obss.meetingmanager.service.google;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleSettingsDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapper;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingService;

import java.util.Map;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMeetingService implements MeetingService {
    private final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final GoogleCalendarServiceClient googleRestClient;
    private final GoogleMapper googleMapper;
    private final KafkaMessageSender messageSender;
    @Value("${topics.email}")
    private String notificationEmailTopic;

    @Override
    @Transactional("ptm")
    public MeetingDTO handleCreate(MeetingDTO meetingDTO, Map<String,Object> settings) {
        GoogleSettingsDTO googleSettings = mapper.convertValue(settings, GoogleSettingsDTO.class);
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,googleSettings,true);
        CalendarEventDTO response = googleRestClient.scheduleEvent(calendarEvent);
        meetingDTO.setCalendarEventId(response.getEventId());
        meetingDTO.setMeetingURL(response.getMeetingUrl());
        log.info("Google Meeting successfully created",meetingDTO.getId());
        return meetingDTO;
    }

    @Transactional("ptm")
    @CircuitBreaker(name ="addMeetingToCalendar", fallbackMethod = "addMeetingToCalendarFallback")
    public void addMeetingToCalendar(MeetingDTO meetingDTO){
        CalendarEventDTO calendarEvent = googleMapper.toCalendarEventDTO(meetingDTO,null,false);
        CalendarEventDTO response = googleRestClient.scheduleEvent(calendarEvent);
        log.info("Meeting successfully added to calendar",meetingDTO.getId());
        meetingDTO.setCalendarEventId(response.getEventId());
    }
    public void addMeetingToCalendarFallback(MeetingDTO meetingDTO,Throwable t){
        log.error("Add Meeting to Calendar Failed",meetingDTO.getId());
        messageSender.sendCreated(notificationEmailTopic,meetingDTO);
    }

    @Transactional("ptm")
    public void sendChangeSlotMail(SlotRequestDTO slotRequestDTO){
        googleRestClient.changeSlot(googleMapper.toGoogleMailDTO(slotRequestDTO));
    }
    @Override
    @Transactional("ptm")
    public MeetingDTO handleUpdate(MeetingDTO meetingDTO,Map<String,Object> settings) {
        GoogleSettingsDTO googleSettings = mapper.convertValue(settings, GoogleSettingsDTO.class);
        updateCalendarMeeting(meetingDTO,googleSettings,true);
        return meetingDTO;
    }
    @Transactional("ptm")
    public void updateCalendarMeeting(MeetingDTO meetingDTO,GoogleSettingsDTO settings,boolean withMeet){
        googleRestClient.updateEvent(googleMapper.toCalendarEventDTO(meetingDTO,settings,withMeet));
        log.info("Google Calendar Event successfully updated",meetingDTO.getId());

    }

    @Override
    @Transactional("ptm")
    public void handleCancel(MeetingDTO meetingDTO) {
        googleRestClient.deleteEvent(new DeleteEventDTO(meetingDTO.getProviderAccount(),
                        meetingDTO.getOrganizer(),meetingDTO.getCalendarEventId()));
        log.info("Google Calendar Event successfully deleted",meetingDTO.getId());
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return GOOGLE;
    }


}
