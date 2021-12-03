package tr.com.obss.meetingmanager.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;
import tr.com.obss.meetingmanager.entity.Meeting;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SlotRequestDTO implements Serializable {
    private static final long serialVersionUID = -4042418997796079829L;
    @NotEmpty
    private String creator;
    @NotEmpty
    private String organizer;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    private long start;
    private long end;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingDTO meeting;
}
