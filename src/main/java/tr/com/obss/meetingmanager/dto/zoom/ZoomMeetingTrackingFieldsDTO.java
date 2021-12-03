package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZoomMeetingTrackingFieldsDTO {
    public String field;

    public String value;

    public Boolean visible;
}
