package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.Optional;

public interface MeetingProviderRepository extends JpaRepository<MeetingProvider, String> {

    Optional<MeetingProvider> findMeetingProviderByMeetingProviderType(MeetingProviderTypeEnum type);



}
