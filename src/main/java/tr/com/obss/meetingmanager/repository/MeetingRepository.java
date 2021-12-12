package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.entity.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, String> {

  @Query(
      "select new Meeting (id,title,startDate,endDate,organizer) from Meeting"
          + " where startDate between ?1 and  ?2")
  List<Meeting> findMeetingsBetweenStartAndEndDate(long startDate, long endDate);

  @EntityGraph(value = "account-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
  Optional<Meeting> findMeetingById(String id);
}
