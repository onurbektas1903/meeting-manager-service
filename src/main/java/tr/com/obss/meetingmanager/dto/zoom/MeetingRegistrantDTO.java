package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MeetingRegistrantDTO implements Serializable {
    private static final long serialVersionUID = -5232621799392132031L;
    public String email;
    public String first_name;
    public String last_name;
}
