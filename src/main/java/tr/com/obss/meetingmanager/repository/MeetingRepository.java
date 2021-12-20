package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.dto.IMeetingOrganizerReportDTO;
import tr.com.obss.meetingmanager.dto.IMeetingTimeReportDTO;
import tr.com.obss.meetingmanager.entity.Meeting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingRepository extends JpaRepository<Meeting, String>,SearchableMeetRepository {

  @Query(
      "select new Meeting (id,title,startDate,endDate,organizer) from Meeting"
          + " where startDate between ?1 and  ?2")
  List<Meeting> findMeetingsBetweenStartAndEndDate(long startDate, long endDate);

  Optional<Meeting> findMeetingById(String id);

  List<Meeting> findByMeetingProviderId(String meetingProviderId);

  @Query(
          "select new java.lang.String (m.providerAccount) from Meeting m"
                  + " where (?1 between m.startDate and m.endDate"
                  + "     or ?2 between m.startDate and m.endDate)"
                  + "     and m.providerAccount in ?3 "
                  + "    ")
  List<String> findFreeAccounts(long startDate,long endDate,Set<String> accountIds);

  @Query(
          value = "select count(*),to_char(to_timestamp( m.start_date/1000 ),'MM/YYYY') as " +
          "yearMonth ,m.meeting_provider_id as providerId from meeting m    " +
          "where m.deleted = false AND m.start_date between ?1 and ?2    " +
          "group by (m.meeting_provider_id,yearMonth)    " +
          "ORDER BY  m.meeting_provider_id ,2 ASC ",nativeQuery=true)
  List<IMeetingTimeReportDTO> findUsageStatistics(long startDate, long endDate);

  @Query(
      value =
          "select count(*) as count,m.organizer from meeting m    "
              + "where m.deleted = false    "
              + "  and m.start_date between ?1 and ?2    "
              + "group by m.organizer    "
              + "order by  count    "
              + "limit 10",
      nativeQuery = true)
  List<IMeetingOrganizerReportDTO> findTopOrganizers(long startDate, long endDate);
}
