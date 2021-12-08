package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.com.obss.meetingmanager.entity.SlotRequest;

import java.util.List;

@Repository
public interface SlotRequestRepository extends JpaRepository<SlotRequest, String> {
    List<SlotRequest> findAllByMeetingId(String meetingId);
    List<SlotRequest> findAllByCreatorAndMeetingId(String creator, String meetingId);
}
