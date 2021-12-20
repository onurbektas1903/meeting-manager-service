package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.entity.GoogleError;

import java.util.List;

public interface GoogleErrorRepository extends JpaRepository<GoogleError, Long> {

    @Query("select M from GoogleError ge inner join Meeting m on ge.meetingId = m.id where m.startDate > ?1 " +
            "and  m.deleted =false")
    List<GoogleError> findMeetingErrors(long date);


}
