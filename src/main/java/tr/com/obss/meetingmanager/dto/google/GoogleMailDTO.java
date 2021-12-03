package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleMailDTO implements Serializable {
    private static final long serialVersionUID = -2131029533242986067L;
    private String accountName;
    private String credentialsFileName;
    private String subject;
    private String senderMail;
    private String recipientMail;
    private String body;
}
