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

    private Boolean join_before_host =false;

    private Boolean mute_upon_entry;

    private Boolean waiting_room;

    private String password;
}
