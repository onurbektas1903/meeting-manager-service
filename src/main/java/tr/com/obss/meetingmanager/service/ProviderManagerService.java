package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.audit.BaseEntity;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.exception.ObjectInUseException;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapper;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderManagerService {

  private final MeetingProviderMapper providerMapper;
  private final MeetingProviderRepository repository;
  private final ProviderAccountManagerService accountManagerService;
  private final MeetingRepository meetingRepository;

  @Transactional("ptm")
  public MeetingProviderDTO saveMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
    Set<String> accounts =
        meetingProviderDTO.getProviderAccounts().parallelStream()
            .map(ProviderAccountDTO::getId)
            .collect(Collectors.toSet());
    List<ProviderAccount> accountList = accountManagerService.findAccountsByIds(accounts);
    MeetingProvider meetingProvider = providerMapper.toEntityWithoutAccounts(meetingProviderDTO);
    accountList.forEach(providerAccount -> providerAccount.setMeetingProvider(meetingProvider));
    meetingProvider.setId(UUID.randomUUID().toString());
    meetingProvider.setProviderAccounts(accountList);
    MeetingProvider saved = repository.save(meetingProvider);
    return providerMapper.toDTO(saved);
  }

  public MeetingProvider getById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Meeting Provider Not Found"));
  }

  public MeetingProviderDTO findById(String id) {
    return providerMapper.toDTO(getById(id));
  }

  public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
    return null;
  }

  public MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
    return null;
  }

  public List<MeetingProviderDTO> getMeetingProviders() {
    return providerMapper.toDTOList(repository.findAll());
  }

  public List<MeetingProviderDTO> getActiveProviders() {
    return providerMapper.toDTOList(repository.findAllByIsActive(true));
  }


  @Transactional("ptm")
  public MeetingProviderDTO activateDeactivateProvider(String id, boolean isActive) {
    MeetingProvider provider = getById(id);
    Set<String> accountIds =
        provider.getProviderAccounts().parallelStream()
            .map(BaseEntity::getId)
            .collect(Collectors.toSet());
    if (!isActive) {
      checkFutureMeetingExistForProviderAccounts(accountIds);
    }
    provider.setIsActive(isActive);
    return providerMapper.toDTO(repository.save(provider));
  }

  private void checkFutureMeetingExistForProviderAccounts(Set<String> accounts) {
    List<Meeting> accountsFutureMeetings =
        meetingRepository.findAccountsFutureMeetings(accounts, Instant.now().toEpochMilli());
    if (!accountsFutureMeetings.isEmpty()) {
      throw new ObjectInUseException(
          "Provider Is Used By A Future Meeting", Collections.singleton("meetingProvider"));
    }
  }
}
