package tr.com.obss.meetingmanager.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SlotRequestDTO implements Serializable {
    private String id;
    private static final long serialVersionUID = -4042418997796079829L;
    @NotEmpty
    private String creator;
    @NotEmpty
    private String organizer;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    private long startDate;
    private long endDate;
    private MeetingProviderTypeEnum type;
    private String meetingId;
    private SlotRequestStatusEnum requestStatus;
}
