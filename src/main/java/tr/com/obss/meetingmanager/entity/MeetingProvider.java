package tr.com.obss.meetingmanager.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class MeetingProvider extends BaseEntity {
    private static final long serialVersionUID = -5859944039923951511L;
    private String name;
    private ConferenceProviderTypeEnum conferenceType;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "meetingProvider")
    @ToString.Exclude
    @Where(clause = "deleted = false")
    @SQLDelete(sql = "UPDATE ProviderAccount SET deleted = true WHERE id = ?")
    private List<ProviderAccount> providerAccounts;
    private Boolean isActive;
    private String userRoleGroup;
    private MeetingProviderTypeEnum meetingProviderType;
    @Type(type = "jsonb")
    private HashMap<String,String> settings;
}
