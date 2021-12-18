package tr.com.obss.meetingmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, String> {

    Optional<ProviderAccount> findByMeetingProviderType(MeetingProviderTypeEnum type);

    @Query(
            "select new tr.com.obss.meetingmanager.entity.ProviderAccount("
                    + "id, applicationName,accountMail,accountDetails)"
                    + " from ProviderAccount "
                    + " where meetingProvider.id = ?3 and id not in "
                    + " ("
                    + " select p.id from ProviderAccount p "
                    + " left outer join Meeting m on(p.id = m.providerAccount) "
                    + " where ((?1 between m.startDate and m.endDate"
                    + "     or ?2 between m.endDate and m.endDate)"
                    + "     and p.meetingProvider.id = ?3 "
                    + "    ))")
    @Lock(PESSIMISTIC_WRITE)
    List<ProviderAccount> findFreeAccounts(long startDate, long endDate, String id);

    Optional<ProviderAccount> findByMeetingProviderId(String id);

    List<ProviderAccount> findAllByMeetingProviderType(MeetingProviderTypeEnum type);

     Optional<List<ProviderAccount>> findAllByIdIn(Set<String> ids);

     Optional<ProviderAccount> findByAccountMailAndMeetingProviderTypeAndIdNot(String accountMail,
                                                                               MeetingProviderTypeEnum type, String id);
 }
