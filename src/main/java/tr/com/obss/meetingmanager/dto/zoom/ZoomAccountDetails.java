package tr.com.obss.meetingmanager.dto.zoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoomAccountDetails {
    @NotEmpty(message = "Api Key cant be empty")
    private String apiKey;
    @NotEmpty(message = "Api Secret  cant be empty")
    private String apiSecret;
}