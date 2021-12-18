package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "googleAccountCache")
@Slf4j
public class GoogleAccountService {
  private final GoogleMapper mapper;
  private final GoogleCalendarServiceClient calendarService;
  private final ProviderAccountManagerService accountManagerService;
  @Transactional("ptm")
  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
  public GoogleAccountDTO createGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile) {
    checkGoogleAccountExists();
    accountManagerService.checkAccountNameIsUniqueForGivenType("-1",googleAccount.getAccountMail(),GOOGLE);
    calendarService.createAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(accountManagerService.saveAccount(mapper.toEntity(googleAccount)));
  }

  @Transactional("ptm")
  @Caching(
          evict = {
                  @CacheEvict(cacheNames = "googleAccount", key = "#id"),
                  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
          })
  public GoogleAccountDTO updateGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile, String id) {
    accountManagerService.checkAccountNameIsUniqueForGivenType(id,googleAccount.getAccountMail(),GOOGLE);
    googleAccount.setAccountDetails(new GoogleAccountDetails());
    calendarService.updateAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(accountManagerService.saveAccount(mapper.toEntity(googleAccount)));
  }

  public GoogleAccountDTO findGoogleAccount(){
    return mapper.toGoogleAccountDTO(accountManagerService.findByMeetingProviderType(GOOGLE));
  }

  @Cacheable(cacheNames = "googleAccounts")
  public List<GoogleAccountDTO> getAll(){
    return mapper.toDTOList(accountManagerService.findAllByMeetingProviderType(GOOGLE));
  }
  @Cacheable(cacheNames = "googleAccount", key = "#id", unless = "#result == null")
  public GoogleAccountDTO findById(String id){
    return mapper.toGoogleAccountDTO(accountManagerService.findProviderAccountById(id));
  }

  @Transactional("ptm")
  @Caching(
          evict = {
                  @CacheEvict(cacheNames = "googleAccount", key = "#id"),
                  @CacheEvict(cacheNames = "googleAccounts", allEntries = true)
          })
  public void deleteAccount(String id ){
    ProviderAccount account = accountManagerService.findProviderAccountById(id);
    checkAccountProviderExists(account);
    accountManagerService.checkFutureMeetingExistForProviderAccounts(id);
    calendarService.deleteAccount(account.getAccountMail());
    accountManagerService.deleteAccount(account);
  }

  @Transactional("ptm")
  public void checkAccountProviderExists(ProviderAccount account) {
    if(account.getMeetingProvider() != null){
      throw new ObjectInUseException("Account referenced by provider",
              Collections.singleton(account.getMeetingProvider().getName()));
    }
  }
  private void checkGoogleAccountExists(){
    try{
      ProviderAccount providerAccount = accountManagerService.findByMeetingProviderType(GOOGLE);
      if(providerAccount != null){
        throw new NotUniqueException("There can only 1 google account",Collections.singleton("googleAccount"));
      }
    }catch(NotFoundException e){
        log.info("Google Account Not Created Yet");
    }
  }

}
