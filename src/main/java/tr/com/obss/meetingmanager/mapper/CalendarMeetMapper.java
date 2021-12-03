package tr.com.obss.meetingmanager.mapper;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;

import java.util.stream.Collectors;

public class CalendarMeetMapper {
    public static CalendarEventDTO toCalendarEventDTO(MeetingDTO meetingDTO){
        return CalendarEventDTO.builder()
                .meetingUrl(meetingDTO.getMeetingURL())
                .start(meetingDTO.getStart())
                .end(meetingDTO.getEnd())
                .summary(meetingDTO.getTitle())
                .description(meetingDTO.getDescription())
                .accountEmail(meetingDTO.getProviderAccountDTO().getAccountMail())
                .adminUserEmail(meetingDTO.getProviderAccountDTO().getAdminUserMail())
                .credentialsFileName(meetingDTO.getProviderAccountDTO().getFileName())
                .createMeeting(false)
                .eventAttendees(meetingDTO.getRecipients()
                        .stream().map(RecipientDTO::getName).collect(Collectors.toSet())).build();
    }
}
