package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProviderAccountDTO implements Serializable {
    private static final long serialVersionUID = -542895211665937754L;
    private String id;
    @NotEmpty(message="account mail can't be null")
    protected String accountMail;
    @NotEmpty(message="applicationName mail can't be null")
    private String applicationName;
    private Boolean isActive;
    private MeetingProviderDTO meetingProvider;
    private MeetingProviderTypeEnum meetingProviderType;
}
