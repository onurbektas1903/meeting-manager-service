package tr.com.obss.meetingmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
@Data
@NoArgsConstructor
public class MeetingProviderDTO {

    private String id;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotNull(message = "conferenceType is mandatory")
    private ConferenceProviderTypeEnum conferenceType;
    private List<ProviderAccountDTO> providerAccounts;
    private boolean isActive;
    private String group;
    @NotNull(message = "meetingProviderType is mandatory")
    private MeetingProviderTypeEnum meetingProviderType;
    private HashMap<String,String> settings;
}
