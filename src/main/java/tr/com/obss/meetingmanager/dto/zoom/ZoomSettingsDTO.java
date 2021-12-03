package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ZoomSettingsDTO implements Serializable {
    private static final long serialVersionUID = 3118522677708467030L;
    private Boolean host_video;

    private Boolean participant_video;

    private Boolean cn_meeting;

    private Boolean in_meeting;

    private Boolean join_before_host;

    private Boolean mute_upon_entry;

    private Boolean watermark;

    private Boolean use_pmi;

    private Integer approval_type;

    private Integer registration_type;

    private String audio;

    private String auto_recording;

    private String alternative_hosts;

    private Boolean close_registration;

    private Boolean waiting_room;
}
