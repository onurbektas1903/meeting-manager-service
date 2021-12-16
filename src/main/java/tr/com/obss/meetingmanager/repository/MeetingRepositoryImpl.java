package tr.com.obss.meetingmanager.repository;

import lombok.RequiredArgsConstructor;
import tr.com.obss.meetingmanager.dto.MeetingQueryDTO;
import tr.com.obss.meetingmanager.dto.QueryObject;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.Recipient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.criteria.JoinType.LEFT;

@RequiredArgsConstructor
public class MeetingRepositoryImpl implements SearchableMeetRepository {
  @PersistenceContext private final EntityManager em;

  @Override
  public List<Meeting> searchMeetings(MeetingQueryDTO queryDTO) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Meeting> cq = cb.createQuery(Meeting.class);
    Root<Meeting> meeting = cq.from(Meeting.class);
    Join<Meeting, Recipient> recipient = meeting.join("recipients", LEFT);

    Predicate titlePredicate = cb.like(meeting.get("title"), "%" + queryDTO.getTitle() + "%");
    Predicate descriptionPredicate =
        cb.like(meeting.get("description"), "%" + queryDTO.getDescription() + "%");
    Predicate recipientPredicate = cb.equal(recipient.get("name"), queryDTO.getRecipient());
    List<Predicate> predList = new LinkedList<>();
    if (queryDTO.getTitle() != null) {
      predList.add(titlePredicate);
    }
    if (queryDTO.getDescription() != null) {
      predList.add(descriptionPredicate);
    }
    if (queryDTO.getRecipient() != null) {
      predList.add(recipientPredicate);
    }
    Predicate[] predArray = new Predicate[predList.size()];
    predList.toArray(predArray);
    CriteriaQuery<Meeting> select =
        cq.select(
            cb.construct(
                Meeting.class,
                meeting.get("id"),
                meeting.get("title"),
                meeting.get("startDate"),
                meeting.get("endDate"),
                meeting.get("organizer")));
    cq.where(predArray);
    cq.orderBy(cb.desc(meeting.get("startDate")));
    QueryObject queryObject = queryDTO.getQueryObject();
    int firstResult = (queryObject.getPageNumber() - 1) * queryObject.getPageSize();
    return em.createQuery(select)
        .setFirstResult(firstResult)
        .setMaxResults(queryObject.getPageSize())
        .getResultList();
  }
}
