package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ZoomMeetingOccurenceDTO implements Serializable {
    private static final long serialVersionUID = -4043280316052955312L;
    private String occurrence_id;

    private String start_time;

    private Integer duration;

    private String status;

}
