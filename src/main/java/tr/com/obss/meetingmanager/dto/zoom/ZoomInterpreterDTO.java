package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ZoomInterpreterDTO implements Serializable {
    private static final long serialVersionUID = -4648814862686246815L;
    public String email;
    public String languages;

}
