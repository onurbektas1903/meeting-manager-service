package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingProviderRepository extends JpaRepository<MeetingProvider, String> {

    @Query("select mp from MeetingProvider mp where mp.deleted = false and mp.isActive = true and ( " +
            " mp.conferenceType = ?1 or mp.userRoleGroup in ?2 or mp.userRoleGroup is null) ")
    List<MeetingProvider> findSuitableProviders(ConferenceProviderTypeEnum type, Set<String> userRoleGroup);

}
