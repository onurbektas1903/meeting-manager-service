package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDTO implements Serializable {
    private static final long serialVersionUID = 281292269426505630L;
    private long start;
    private long end;
    private String title;
    private String description;
    private Set<String> eventAttendees;
    private String meetingUrl;
    private boolean createMeeting;
    private String summary;
    private String creator;
    private String eventId;
    private String accountId;
}
