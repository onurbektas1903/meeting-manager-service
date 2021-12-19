package tr.com.obss.meetingmanager.mapper.google;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleMailDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.stream.Collectors;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Component
public class GoogleMapper {

    public  GoogleMailDTO toGoogleMailDTO(SlotRequestDTO slotRequestDTO){
        return
                GoogleMailDTO.builder()
                        .subject(slotRequestDTO.getTitle())
                        .body(slotRequestDTO.getDescription())
                        .senderMail(slotRequestDTO.getCreator())
                        .recipientMail(slotRequestDTO.getOrganizer())
                        .meetingLink(slotRequestDTO.getMeetingLink())
                        .build();
    }

    public CalendarEventDTO toCalendarEventDTO(MeetingDTO meetingDTO,boolean withMeet){
        MeetingProviderTypeEnum type = meetingDTO.getMeetingProvider().getMeetingProviderType();
        return CalendarEventDTO.builder()
                .meetingUrl(meetingDTO.getMeetingURL())
                .start(meetingDTO.getStart())
                .creator(meetingDTO.getOrganizer())
                .end(meetingDTO.getEnd())
                .summary(meetingDTO.getTitle())
                .description(type == GOOGLE ? meetingDTO.getDescription() : (meetingDTO.getDescription() + " " + meetingDTO.getMeetingURL()))
                .meetingUrl(meetingDTO.getMeetingURL())
                .accountId(meetingDTO.getProviderAccount())
                .eventId(meetingDTO.getCalendarEventId())
                .createMeeting(withMeet)
                .eventAttendees(meetingDTO.getRecipients()
                        .stream().map(RecipientDTO::getName).collect(Collectors.toSet())).build();
    }
}
