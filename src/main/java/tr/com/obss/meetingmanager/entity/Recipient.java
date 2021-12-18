package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Where(clause = "deleted = false")
@NoArgsConstructor

public class Recipient extends BaseEntity {

    private static final long serialVersionUID = -5120769224736206346L;
    private String name;
    private Boolean emailReceived;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;
}
