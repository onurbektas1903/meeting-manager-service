package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MeetingDTO implements Serializable {

    private static final long serialVersionUID = 7280979599164463756L;
    private String id;
    @NotBlank(message = "title is mandatory")
    private String title;
    private long start;
    private long end;
    private long duration;
    private String description;
    @NotBlank(message = "organizer is mandatory")
    private String organizer;
    private String meetingURL;
    private List<RecipientDTO> recipients;
    private List<SlotRequestDTO> slotRequests;
    private MeetingProviderTypeEnum meetingProviderType;
    @NotNull(message = "meetingProvider is mandatory")
    private  MeetingProviderDTO meetingProvider;
    private String providerAccount;
    private String calendarEventId;
    private String eventId;

}
