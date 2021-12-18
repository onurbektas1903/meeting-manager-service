package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.MeetingOccupiedException;
import tr.com.obss.meetingmanager.exception.NotUniqueException;
import tr.com.obss.meetingmanager.exception.ObjectInUseException;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProviderAccountManagerService {
    private final ProviderAccountRepository repository;
    private final MeetingRepository meetingRepository;


    public ProviderAccount findByMeetingProviderType(MeetingProviderTypeEnum type){
        return repository.findByMeetingProviderType(type).orElseThrow(() ->
                new NotFoundException("Account Not Found", Collections.singleton(type.toString())));
    }

    @Transactional("ptm")
    public ProviderAccount saveAccount(ProviderAccount account){
        return repository.save(account);
    }

    @Transactional("ptm")
    public void deleteAccount(ProviderAccount account){
         repository.delete(account);
    }

    public List<ProviderAccount> findAllByMeetingProviderType(MeetingProviderTypeEnum type){
        return repository.findAllByMeetingProviderType(type);
    }

    public void checkAccountNameIsUniqueForGivenType(String id,String accountMail ,MeetingProviderTypeEnum type){
        Optional<ProviderAccount> providerAccount =
                repository.findByAccountMailAndMeetingProviderTypeAndIdNot(accountMail, type,id);
        if(providerAccount.isPresent()){
            throw new NotUniqueException("Account name with this type already in use",Collections.singleton("accountMail"));
        }
    }

    public ProviderAccount findProviderAccountById(String id){
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException("Account Not Found", Collections.singleton(id)));
    }

    public ProviderAccount getSuitableAccount(long startDate, long endDate, MeetingProviderDTO providerDTO){
        return providerDTO.getConferenceType() == ConferenceProviderTypeEnum.POOL ?
                findFreeAccountsForGivenDateRange(startDate, endDate, providerDTO) :
                findAccountByProviderId(providerDTO.getId());
    }

    public ProviderAccount findAccountByProviderId(String providerId){
       return repository.findByMeetingProviderId(providerId).orElseThrow(()-> new NotFoundException(
                "Active Account Not Found",Collections.singleton("account")));
    }
    public List<ProviderAccount> findFreeProviderAccounts(MeetingProviderTypeEnum type,String providerId){
       return repository.findByMeetingProviderTypeAndMeetingProviderIsNullOrMeetingProviderId(type,providerId);
    }

    private ProviderAccount findFreeAccountsForGivenDateRange(long startDate, long endDate, MeetingProviderDTO providerDTO) {
        List<ProviderAccount> freeAccounts =repository.findFreeAccounts(startDate, endDate, providerDTO.getId());
        if(freeAccounts.isEmpty()){
            throw  new MeetingOccupiedException("Meeting occupied for selected provider",
                    Collections.singleton(providerDTO.getName()));
        }
        return freeAccounts.get(0);
    }

    @Transactional("ptm")
    public List<ProviderAccount> findAccountsByIds( Set<String> accounts){
       return repository.findAllByIdIn(accounts).orElseThrow(()-> new NotFoundException(
                "Accounts Not Found",Collections.singleton("providerAccounts")));
    }


    public void checkFutureMeetingExistForProviderAccounts(String account) {
        List<Meeting> accountsFutureMeetings =
                meetingRepository.findAccountsFutureMeetings(account, Instant.now().toEpochMilli());
        if (!accountsFutureMeetings.isEmpty()) {
            throw new ObjectInUseException(
                    "Provider Is Used By A Future Meeting", Collections.singleton("account"));
        }
    }
}
