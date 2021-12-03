package tr.com.obss.meetingmanager.dto.zoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ZoomProviderAccountDetail implements Serializable {

    private static final long serialVersionUID = -7613145535055916247L;
    @NotEmpty(message = "Api Key Cannot be null")
    private String apiKey;
    @NotEmpty(message = "Api Secret Cannot be null")
    private String apiSecret;
}
