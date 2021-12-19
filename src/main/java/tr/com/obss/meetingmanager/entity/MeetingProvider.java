package tr.com.obss.meetingmanager.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import tr.com.common.audit.AuditableEntity;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class MeetingProvider extends AuditableEntity {
    private static final long serialVersionUID = -5859944039923951511L;
    private String name;
    private ConferenceProviderTypeEnum conferenceType;
    private Boolean isActive;
    private String userRoleGroup;
    private MeetingProviderTypeEnum meetingProviderType;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String,String> settings = new HashMap<>();
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String,String> accounts = new HashMap<>();
}
