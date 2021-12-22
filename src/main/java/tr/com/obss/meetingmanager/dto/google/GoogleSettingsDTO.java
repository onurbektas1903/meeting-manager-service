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
public class GoogleSettingsDTO implements Serializable {
    private static final long serialVersionUID = -1615738167514393144L;
    private boolean muted;
    private boolean canOthersHost;
    private boolean guestsCanModify;
    private boolean guestsCanInviteOthers;
    private String password;
}
