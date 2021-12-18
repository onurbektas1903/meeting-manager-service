package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDetails;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.exception.NotUniqueException;
import tr.com.obss.meetingmanager.exception.ObjectInUseException;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapper;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "googleAccountCache")
public class GoogleAccountService {
  private final ProviderAccountRepository repository;
  private final GoogleMapper mapper;
  private final GoogleCalendarServiceClient calendarService;

  @Transactional("ptm")
  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
  public GoogleAccountDTO createGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile) {
      if(repository.findByMeetingProviderType(GOOGLE).isPresent()){
        throw new NotUniqueException("There can only be 1 google account",Collections.singleton("googleAccount"));
      }
    checkAccountNameIsUniqueForGivenType(googleAccount);
    calendarService.createAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(repository.save(mapper.toEntity(googleAccount)));
  }

  @Transactional("ptm")
  @Caching(
          evict = {
                  @CacheEvict(cacheNames = "googleAccount", key = "#id"),
                  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
          })
  public GoogleAccountDTO updateGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile, String id) {
    checkAccountNameIsUniqueForGivenType(googleAccount);
    googleAccount.setAccountDetails(new GoogleAccountDetails());
    calendarService.updateAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(repository.save(mapper.toEntity(googleAccount)));
  }

  public GoogleAccountDTO findGoogleAccount(){
    ProviderAccount providerAccount = repository.findByMeetingProviderType(GOOGLE).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
    return mapper.toGoogleAccountDTO(providerAccount);
  }
  @Cacheable(cacheNames = "googleAccounts")
  public List<GoogleAccountDTO> getAll(){
    return mapper.toDTOList(repository.findAllByMeetingProviderType(GOOGLE));
  }
  @Cacheable(cacheNames = "googleAccount", key = "#id", unless = "#result == null")
  public GoogleAccountDTO findById(String id){
    ProviderAccount providerAccount = findProviderAccountById(id);
    return mapper.toGoogleAccountDTO(providerAccount);
  }

  @Transactional("ptm")
  @Caching(
          evict = {
                  @CacheEvict(cacheNames = "googleAccount", key = "#id"),
                  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
          })
  public void deleteAccount(String id ){
    ProviderAccount account = findProviderAccountById(id);
    checkAccountProviderExists(account);
    calendarService.deleteAccount(account.getAccountMail());
    repository.delete(account);
  }

  @Transactional("ptm")
  public void checkAccountProviderExists(ProviderAccount account) {
    if(account.getMeetingProvider() != null){
      throw new ObjectInUseException("Account referenced by provider",
              Collections.singleton(account.getMeetingProvider().getName()));
    }
  }
  @Cacheable(cacheNames = "googleAccount", key = "#id", unless = "#result == null")
  public ProviderAccount findProviderAccountById(String id) {
    return repository.findById(id).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
  }

  public void checkAccountNameIsUniqueForGivenType(GoogleAccountDTO googleAccountDTO){
    Optional<ProviderAccount> providerAccount =
            repository.findByAccountMailAndMeetingProviderTypeAndIdNot(googleAccountDTO.getAccountMail(), GOOGLE,
                    googleAccountDTO.getId() == null ? "-1" : googleAccountDTO.getId());
    if(providerAccount.isPresent()){
      throw new NotUniqueException("Account name with this type already in use",Collections.singleton("accountMail"));
    }
  }

}
