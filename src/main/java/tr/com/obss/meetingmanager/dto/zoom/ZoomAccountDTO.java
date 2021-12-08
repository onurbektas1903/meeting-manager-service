package tr.com.obss.meetingmanager.dto.zoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ZoomAccountDTO extends ProviderAccountDTO {
     private static final long serialVersionUID = -1041615043712747023L;
     private ZoomAccountDetails accountDetails;
}