package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.common.exceptions.NotFoundException;
import tr.com.common.exceptions.ObjectInUseException;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.factory.MeetProviderHandlerFactory;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapper;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "providerCache")
public class ProviderManagerService {

  private final MeetProviderHandlerFactory handlerFactory;
  private final MeetingProviderMapper providerMapper;
  private final MeetingProviderRepository repository;
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
    MeetingProvider meetingProvider = providerMapper.toEntity(meetingProviderDTO);
    return providerMapper.toDTO(repository.save(meetingProvider));
  }

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
    return providerMapper.toDTO(repository.save(provider));
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
