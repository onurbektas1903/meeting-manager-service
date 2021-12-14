package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccountDetails implements Serializable {
    private static final long serialVersionUID = -8829019005566464109L;
    private String adminUserEmail;
    private String credentialsFileName;
}