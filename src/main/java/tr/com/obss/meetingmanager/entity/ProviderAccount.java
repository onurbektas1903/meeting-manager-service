package tr.com.obss.meetingmanager.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.AuditableEntity;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ProviderAccount extends AuditableEntity {
    private static final long serialVersionUID = -1278099486349939102L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_provider_id")
    private MeetingProvider meetingProvider;
    private String applicationName;
    private String accountMail;
    private MeetingProviderTypeEnum meetingProviderType;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String,String> accountDetails;
    private Boolean isActive;

    public ProviderAccount(String id,String applicationName, String accountMail,
                           Object accountDetails,
                           Boolean isActive) {
        this.setId(id);
        this.applicationName = applicationName;
        this.accountMail = accountMail;
        this.accountDetails = (Map<String, String>) accountDetails;
        this.isActive = isActive;
    }
}
