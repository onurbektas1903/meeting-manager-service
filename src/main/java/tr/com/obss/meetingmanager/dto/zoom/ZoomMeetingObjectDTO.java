package tr.com.obss.meetingmanager.dto.zoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ZoomMeetingObjectDTO implements Serializable {


    private static final long serialVersionUID = 864081454263918222L;
    private String id;

    private String uuid;

    private String host_email;

    private String topic;

    private Integer type;

    private String start_time;

    private Integer duration;

    private String schedule_for;

    private String timezone;

    private String created_at;

    private String password;

    private String start_url;

    private String join_url;

    private  ZoomSettingsDTO settings;

    private String accountId;

}
