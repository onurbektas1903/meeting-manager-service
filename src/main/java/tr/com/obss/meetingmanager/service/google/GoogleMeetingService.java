package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleMailDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.ProviderManagerService;

import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE_MEET;
import static tr.com.obss.meetingmanager.mapper.CalendarMeetMapper.toCalendarEventDTO;

@Service
@RequiredArgsConstructor
public class GoogleMeetingService implements MeetingService {
    @Value("${topics.notification}")
    private String notificationTopic;
    private final MeetingManagerService meetingManagerService;
    private final ProviderManagerService providerManagerService;
    private final GoogleCalendarServiceClient calendarClientService;
    private final KafkaMessageSender messageSender;
    @Override
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        MeetingProviderDTO meetingProvider = providerManagerService.findByMeetingProviderType(GOOGLE_MEET);

        meetingDTO.setProviderAccountDTO(meetingProvider.getProviderAccounts().get(0));
        CalendarEventDTO calendarEvent = toCalendarEventDTO(meetingDTO);
        calendarEvent.setCreateMeeting(true);

//        CalendarEventDTO response = calendarClientService.scheduleMeeting(calendarEvent);
//        meetingDTO.setMeetingURL(response.getMeetingUrl());
        //        messageSender.sendCreated(notificationTopic,new DomainMessage(CREATED,savedMeeting));
        return meetingManagerService.saveMeeting(meetingDTO);
    }
    public void sendChangeSlotMail(SlotRequestDTO slotRequestDTO){
        MeetingProviderDTO meetingProvider = providerManagerService.findByMeetingProviderType(GOOGLE_MEET);
        ProviderAccountDTO account = meetingProvider.getProviderAccounts().get(0);

    GoogleMailDTO mailDTO =
        GoogleMailDTO.builder()
                .subject(slotRequestDTO.getTitle())
                .body(slotRequestDTO.getDescription())
                .senderMail(slotRequestDTO.getCreator())
                .accountName(account.getAdminUserMail())
                .recipientMail(slotRequestDTO.getMeeting().getOrganizer())
                .credentialsFileName(account.getFileName())
            .build();
        calendarClientService.changeSlot(mailDTO);
    }
    @Override
    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
        return null;
    }

    @Override
    public MeetingDTO deleteMeeting(MeetingDTO meetingDTO) {
        return null;
    }

    @Override
    public List<MeetingDTO> listMeetings() {
        return null;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return GOOGLE_MEET;
    }
}
