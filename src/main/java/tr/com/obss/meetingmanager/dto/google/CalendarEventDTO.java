package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CalendarEventDTO implements Serializable {
    private static final long serialVersionUID = 7067146390253236505L;
    private long start;
    private long end;
    private String title;
    private String description;
    private Set<String> eventAttendees;
    private String meetingUrl;
    private boolean createMeeting;
    private String summary;
    private String creator;
    private String accountEmail;
    private String adminUserEmail;
    private String credentialsFileName;
    private String meetingId;

}
