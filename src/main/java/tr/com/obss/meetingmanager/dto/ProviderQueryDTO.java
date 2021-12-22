package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderQueryDTO implements Serializable {
    private static final long serialVersionUID = 7155807387732925388L;
    Set<String> roleGroups;
}
