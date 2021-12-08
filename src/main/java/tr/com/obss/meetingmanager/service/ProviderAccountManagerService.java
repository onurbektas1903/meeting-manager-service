package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.MeetingOccupiedException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProviderAccountManagerService {
    private final ProviderAccountRepository repository;

    public ProviderAccount getSuitableAccount(long startDate, long endDate, MeetingProviderDTO providerDTO){
        return providerDTO.getConferenceType() == POOL ?
                findFreeAccountsForGivenDateRange(startDate, endDate, providerDTO) :
                findActiveAccountByProviderId(providerDTO.getId());
    }

    public ProviderAccount findActiveAccountByProviderId(String providerId){
       return repository.findByMeetingProviderIdAndIsActive(providerId,true).orElseThrow(()-> new NotFoundException(
                "Active Account Not Found",Collections.singleton("providerAccount")));
    }
    private ProviderAccount findFreeAccountsForGivenDateRange(long startDate, long endDate, MeetingProviderDTO providerDTO) {
        List<ProviderAccount> freeAccounts =repository.findFreeAccounts(startDate, endDate, providerDTO.getId());
        if(freeAccounts.isEmpty()){
            throw  new MeetingOccupiedException("Meeting occupied for selected provider",
                    Collections.singleton(providerDTO.getName()));
        }
        return freeAccounts.get(0);
    }
    @Transactional
    public List<ProviderAccount> findAccountsByIds( Set<String> accounts){
       return repository.findAllByIdIn(accounts).orElseThrow(()-> new NotFoundException(
                "Accounts Not Found",Collections.singleton("providerAccounts")));
    }

}
