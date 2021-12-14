package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
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
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleAccountService {
  private final ProviderAccountRepository repository;
  private final MeetingRepository meetingRepository;
  private final GoogleMapper mapper;
  private final GoogleCalendarServiceClient calendarService;
  @Transactional("ptm")
  public GoogleAccountDTO createGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile) {
      if(repository.findByIsActiveAndMeetingProviderType(true,GOOGLE).isPresent()){
        googleAccount.setIsActive(false);
      }
    checkAccountNameIsUniqueForGivenType(googleAccount);
    calendarService.createAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(repository.save(mapper.toEntity(googleAccount)));
  }
  @Transactional("ptm")
  public GoogleAccountDTO updateGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile, String id) {
    checkAccountNameIsUniqueForGivenType(googleAccount);
    googleAccount.setAccountDetails(new GoogleAccountDetails());
    calendarService.updateAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(repository.save(mapper.toEntity(googleAccount)));
  }

  public GoogleAccountDTO findActiveAccount(){
    ProviderAccount providerAccount = repository.findByIsActiveAndMeetingProviderType(true,GOOGLE).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
    return mapper.toGoogleAccountDTO(providerAccount);
  }
  public List<GoogleAccountDTO> getAll(){
    return mapper.toDTOList(repository.findAllByMeetingProviderType(GOOGLE));
  }

  public GoogleAccountDTO findById(String id){
    ProviderAccount providerAccount = findProviderAccountById(id);
    return mapper.toGoogleAccountDTO(providerAccount);
  }
  @Transactional("ptm")
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

  private ProviderAccount findProviderAccountById(String id) {
    return repository.findById(id).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
  }

  private void checkAccountNameIsUniqueForGivenType(GoogleAccountDTO googleAccountDTO){
    Optional<ProviderAccount> providerAccount =
            repository.findByAccountMailAndMeetingProviderTypeAndIdNot(googleAccountDTO.getAccountMail(), GOOGLE,
                    googleAccountDTO.getId() == null ? "-1" : googleAccountDTO.getId());
    if(providerAccount.isPresent()){
      throw new NotUniqueException("Account name with this type already in use",Collections.singleton("accountMail"));
    }
  }

}
