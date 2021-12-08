package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class GoogleMeetingSettings implements Serializable {
    private static final long serialVersionUID = -1615738167514393144L;
    private Boolean muted;
    private Boolean canOthersHost;
}
