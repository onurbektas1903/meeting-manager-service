package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MeetingProviderDTO implements Serializable {

    private static final long serialVersionUID = -8392351073956109504L;
    private String id;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotNull(message = "conferenceType is mandatory")
    private ConferenceProviderTypeEnum conferenceType;
    private List<ProviderAccountDTO> providerAccounts;
    private Boolean isActive;
    private String userRoleGroup;
    @NotNull(message = "meetingProviderType is mandatory")
    private MeetingProviderTypeEnum meetingProviderType;
    private HashMap<String,String> settings;
}
