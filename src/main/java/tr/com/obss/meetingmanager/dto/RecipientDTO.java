package tr.com.obss.meetingmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDTO implements Serializable {
    private static final long serialVersionUID = 4137261646651244399L;
    private String id;
    private String name;
}
