package tr.com.obss.meetingmanager.dto.zoom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ZoomGlobalDialInNumbersDTO implements Serializable {
    private static final long serialVersionUID = -5055138780142154101L;
    private String country;

    private String country_name;

    private String city;

    private String number;

    private String type;
}
