package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccountDetails  {
    private String adminUserEmail;
    @NotEmpty(message = "Credentials file cant be empty")
    private String credentialsFileName;
}