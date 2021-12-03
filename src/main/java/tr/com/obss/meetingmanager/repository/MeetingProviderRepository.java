package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;
import java.util.Optional;

public interface MeetingProviderRepository extends JpaRepository<MeetingProvider, String> {

    Optional<MeetingProvider> findMeetingProviderByMeetingProviderType(MeetingProviderTypeEnum type);

    @Query(
            "select new tr.com.obss.meetingmanager.dto.ProviderAccountDTO(id,adminUserMail,accountId,clientId,clientSecret," +
                    "fileName)  "
                    + " from ProviderAccount "
                    + "where meetingProvider.id = ?3 and id not in "
                    + " ("
                    + "select p.id from ProviderAccount p "
                    + "left outer join Meeting m on(p.id = m.providerAccount) "
                    + "where ((?1 between m.startDate and m.endDate"
                    + "     or ?2 between m.endDate and m.endDate)"
                    + "     and p.meetingProvider.id = ?3 "
                    + "    ))")
    List<ProviderAccountDTO> findFreeAccounts(long startDate, long endDate, String id);


}
