package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.entity.Meeting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingRepository extends JpaRepository<Meeting, String>,SearchableMeetRepository {

  @Query(
      "select new Meeting (id,title,startDate,endDate,organizer) from Meeting"
          + " where startDate between ?1 and  ?2")
  List<Meeting> findMeetingsBetweenStartAndEndDate(long startDate, long endDate);

  @EntityGraph(value = "account-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
  Optional<Meeting> findMeetingById(String id);

  @Query(
          "select new Meeting (id,title,startDate,endDate,organizer) from Meeting"
                  + " where providerAccount.id in ?1 and startDate >= ?2")
  List<Meeting> findAccountsFutureMeetings(Set<String> accounts, long now);
//  @Query(
//          "select new Meeting (id,title,startDate,endDate,organizer) from Meeting"
//                  + " where providerAccount.id = ?1 and startDate >= ?2")
//  List<Meeting> findAccountsFutureMeetings(String accountId,long now);

}
