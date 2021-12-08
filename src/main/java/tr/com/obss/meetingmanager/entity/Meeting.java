package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tr.com.obss.meetingmanager.audit.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Where(clause = "deleted = false")
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "account-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "providerAccount", subgraph = "providerAccount-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "providerAccount-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("meetingProvider")
                        }
                )
        }
)
public class Meeting extends BaseEntity {

    private static final long serialVersionUID = -6694299942650744346L;
    private String title;
    private long startDate;
    private long endDate;
    private String description;
    private String organizer;
    private String meetingURL;
    private String eventId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "meeting")
    @Where(clause = "deleted = false")
    @SQLDelete(sql = "UPDATE Recipient SET deleted = true WHERE id = ?")
    private List<Recipient> recipients;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_account_id", nullable = false)
    private ProviderAccount providerAccount;

    public Meeting(String id,String title, long startDate, long endDate, String organizer) {
        setId(id);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organizer = organizer;
    }
}
