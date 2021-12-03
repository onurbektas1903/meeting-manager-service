package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderAccountDTO implements Serializable {
    private static final long serialVersionUID = -542895211665937754L;
    private String id;
    private String adminUserMail;
    private String accountMail;
    private String clientId;
    private String clientSecret;
    private String applicationName;
}
