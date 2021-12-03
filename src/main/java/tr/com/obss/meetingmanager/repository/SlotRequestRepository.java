package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.obss.meetingmanager.entity.SlotRequest;

public interface SlotRequestRepository extends JpaRepository<SlotRequest, String> {

}
