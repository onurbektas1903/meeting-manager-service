package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingQueryDTO implements Serializable {
    private static final long serialVersionUID = 1922327674423046736L;
    private String title;
    private String description;
    private String recipient;
    private Long start;
    private Long end;
    private QueryObject queryObject;
}
