package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tr.com.common.audit.AuditableEntity;
import tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
public class SlotRequest extends AuditableEntity {
    private static final long serialVersionUID = 7515289605959696408L;
    private String creator;
    private String organizer;
    private String title;
    private String description;
    private long startDate;
    private long endDate;
    private SlotRequestStatusEnum requestStatus;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;
}
