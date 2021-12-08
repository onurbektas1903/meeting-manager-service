package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;
import tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
public class SlotRequest extends BaseEntity {
    private static final long serialVersionUID = 7515289605959696408L;
    private String creator;
    private String organizer;
    private String title;
    private String description;
    private long startDate;
    private long endDate;
    private String meetingId;
    private SlotRequestStatusEnum requestStatus;
}
