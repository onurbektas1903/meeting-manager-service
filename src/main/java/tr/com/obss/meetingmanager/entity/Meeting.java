package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tr.com.common.audit.AuditableEntity;

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
@SQLDelete(sql = "UPDATE meeting SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@Entity
@ToString(exclude = {"recipients","slotRequests"})
public class Meeting extends AuditableEntity {

  private static final long serialVersionUID = -6694299942650744346L;
  private String title;
  private long startDate;
  private long endDate;
  private String description;
  private String organizer;
  private String meetingURL;
  private String calendarEventId;
  private String eventId;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "meeting",
      orphanRemoval = true)
  @Where(clause = "deleted = false")
  @SQLDelete(sql = "UPDATE Recipient SET deleted = true WHERE id = ?")
  private List<Recipient> recipients;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "meeting",
      orphanRemoval = true)
  @Where(clause = "deleted = false")
  @SQLDelete(sql = "UPDATE SlotRequest SET deleted = true WHERE id = ?")
  private List<SlotRequest> slotRequests;

  private String providerAccount;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "meeting_provider_id", nullable = false)
  private MeetingProvider meetingProvider;

  public Meeting(String id, String title, long startDate, long endDate, String organizer) {
    setId(id);
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.organizer = organizer;
  }

  public void addSlotRequest(SlotRequest slotRequest) {
    this.slotRequests.add(slotRequest);
    slotRequest.setMeeting(this);
  }
}
