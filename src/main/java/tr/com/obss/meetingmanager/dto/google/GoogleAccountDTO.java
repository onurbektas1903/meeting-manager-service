package tr.com.obss.meetingmanager.dto.google;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;

import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoogleAccountDTO extends ProviderAccountDTO implements Serializable {
    private static final long serialVersionUID = -3237577050208091033L;
    private GoogleAccountDetails accountDetails;
}