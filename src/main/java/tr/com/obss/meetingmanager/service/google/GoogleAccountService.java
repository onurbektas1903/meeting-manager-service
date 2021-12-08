package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.exception.NotUniqueException;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapper;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;

import java.util.Collections;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleAccountService {
  private final ProviderAccountRepository repository;
  private final GoogleMapper mapper;
  private final GoogleCalendarServiceClient calendarService;

  public GoogleAccountDTO createGoogleAccount(GoogleAccountDTO googleAccount, MultipartFile multipartFile) {
    checkActiveAccountExists();
    calendarService.createAccount(multipartFile,googleAccount.getAccountMail());
    return mapper.toGoogleAccountDTO(repository.save(mapper.toEntity(googleAccount)));
  }

  public GoogleAccountDTO findActiveAccount(){
    ProviderAccount providerAccount = repository.findByIsActiveAndMeetingProviderType(true,GOOGLE).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
    return mapper.toGoogleAccountDTO(providerAccount);
  }
  public void checkActiveAccountExists(){
      if (repository.findByIsActiveAndMeetingProviderType(true,GOOGLE).isPresent()){
          throw new NotUniqueException("Can not be more than 1 active Google Account",Collections.singleton(
                  "googleAccount"));
      }
  }
  public List<GoogleAccountDTO> getAll(){
    return mapper.toDTOList(repository.findAllByMeetingProviderType(GOOGLE));
  }

  public GoogleAccountDTO findById(String id){
    ProviderAccount providerAccount = repository.findById(id).orElseThrow(() ->
            new NotFoundException("Account Not Found", Collections.singleton("googleAccount")));
    return mapper.toGoogleAccountDTO(providerAccount);
  }
}
