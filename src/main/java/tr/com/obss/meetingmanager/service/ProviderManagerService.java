package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.exception.ObjectInUseException;
import tr.com.obss.meetingmanager.factory.MeetProviderHandlerFactory;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapper;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "providerCache")
public class ProviderManagerService {

  private final MeetProviderHandlerFactory handlerFactory;
  private final MeetingProviderMapper providerMapper;
  private final MeetingProviderRepository repository;
  private final ProviderAccountManagerService accountManagerService;
  private final MeetingRepository meetingRepository;

  @Transactional("ptm")
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "providers", allEntries = true),
        @CacheEvict(cacheNames = "activeProviders", allEntries = true)
      })
  public MeetingProviderDTO saveMeetingProvider(MeetingProviderDTO meetingProviderDTO) {

    handlerFactory
            .findStrategy(meetingProviderDTO.getMeetingProviderType())
            .createMeetingProvider(meetingProviderDTO);
    MeetingProvider meetingProvider = providerMapper.toEntityWithoutAccounts(meetingProviderDTO);
    setAccounts(meetingProviderDTO, meetingProvider);
    return providerMapper.toDTO(repository.save(meetingProvider));
  }

  @Cacheable(cacheNames = "provider", key = "#id", unless = "#result == null")
  public MeetingProvider getById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Meeting Provider Not Found"));
  }

  @Cacheable(cacheNames = "provider", key = "#id", unless = "#result == null")
  public MeetingProviderDTO findById(String id) {
    return providerMapper.toDTO(getById(id));
  }

  @Caching(
      evict = {
        @CacheEvict(cacheNames = "provider", key = "#id"),
        @CacheEvict(cacheNames = "providers", allEntries = true),
        @CacheEvict(cacheNames = "activeProviders", allEntries = true)
      })
  @Transactional("ptm")
  public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO, String id) {
    MeetingProvider provider = getById(id);
    providerMapper.updateProvider(meetingProviderDTO, provider);
    handlerFactory
            .findStrategy(meetingProviderDTO.getMeetingProviderType())
            .createMeetingProvider(meetingProviderDTO);
    setAccounts(meetingProviderDTO, provider);
    return providerMapper.toDTO(repository.save(provider));
  }

  @Transactional("ptm")
  public void setAccounts(MeetingProviderDTO meetingProviderDTO, MeetingProvider provider) {
    Set<String> accounts =
        meetingProviderDTO.getProviderAccounts().parallelStream()
            .map(ProviderAccountDTO::getId)
            .collect(Collectors.toSet());
    List<ProviderAccount> accountList = accountManagerService.findAccountsByIds(accounts);
    provider.getProviderAccounts().forEach(providerAccount ->{
      if(!accounts.contains(providerAccount.getId())){
        providerAccount.setMeetingProvider(null);
      }
    });
    provider.addProviderAccounts(accountList);
  }
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "provider", key = "#id"),
        @CacheEvict(cacheNames = "providers", allEntries = true),
        @CacheEvict(cacheNames = "activeProviders", allEntries = true)
      })
  @Transactional("ptm")
  public void deleteMeetingProvider(String id) {
    MeetingProvider provider = getById(id);
    checkProviderUsedByAnyMeeting(id);
    provider.getProviderAccounts().forEach(providerAccount -> providerAccount.setMeetingProvider(null));
    repository.delete(provider);
  }

  @Cacheable(cacheNames = "providers")
  public List<MeetingProviderDTO> getMeetingProviders() {
    return providerMapper.toDTOList(repository.findAll());
  }

  @Cacheable(cacheNames = "activeProviders")
  public List<MeetingProviderDTO> getActiveProviders() {
    return providerMapper.toDTOList(repository.findAllByIsActive(true));
  }

  @Transactional("ptm")
  @Caching(
      evict = {
        @CacheEvict(cacheNames = "provider", key = "#id"),
        @CacheEvict(cacheNames = "providers", allEntries = true),
        @CacheEvict(cacheNames = "activeProviders", allEntries = true)
      })
  public MeetingProviderDTO activateDeactivateProvider(String id, boolean isActive) {
    MeetingProvider provider = getById(id);
    provider.setIsActive(isActive);
    return providerMapper.toDTO(repository.save(provider));
  }

  private void checkProviderUsedByAnyMeeting(String providerId) {
    List<Meeting> meetings = meetingRepository.findByMeetingProviderId(providerId);
    if (meetings != null && !meetings.isEmpty()) {
      throw new ObjectInUseException(
          "Provider account used by meeting", Collections.singleton("providerAccount"));
    }
  }
}
