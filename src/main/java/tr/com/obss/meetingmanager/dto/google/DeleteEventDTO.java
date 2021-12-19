package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DeleteEventDTO implements Serializable {
    private static final long serialVersionUID = -7748878249651705322L;
    private String accountId;
    private String creator;
    private String eventId;
}
