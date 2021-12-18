package tr.com.obss.meetingmanager.meeting.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.exception.BusinessValidationException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;
import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.SINGLE;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderAccountTestFactory.createProviderAccount;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderAccountTestFactory.createProviderAccountList;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.PROVIDER_ID;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderDTO;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.END_DATE;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.START_DATE;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ProviderAccountServiceTest {

  private ProviderAccountManagerService providerAccountManagerService;
  private @Mock ProviderAccountRepository repository;
  private @Mock MeetingRepository meetingRepository;

  @BeforeEach
  public void init() {
    providerAccountManagerService = new ProviderAccountManagerService(repository,meetingRepository);
    Mockito.lenient()
        .when(repository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createProviderAccount(GOOGLE)));
  }

  @Test
  public void getSuitableAccount_shouldFireFindFreeAccountIfTypeIsPOOL() {

    Mockito.lenient()
            .when(repository.findFreeAccounts(START_DATE,END_DATE, PROVIDER_ID))
            .thenReturn(createProviderAccountList());
    MeetingProviderDTO meetingProviderDTO = createMeetingProviderDTO(GOOGLE);
    meetingProviderDTO.setConferenceType(POOL);
    providerAccountManagerService.getSuitableAccount(START_DATE,END_DATE,meetingProviderDTO);
    verify(repository,times(1)).findFreeAccounts(START_DATE,END_DATE, PROVIDER_ID);
  }

  @Test
  public void getSuitableAccount_shouldFireFindByProviderIdIfTypeIsSINGLE() {
    Mockito.lenient()
            .when(repository.findByMeetingProviderId(Mockito.anyString()))
            .thenReturn(Optional.of(createProviderAccount(GOOGLE)));
    MeetingProviderDTO meetingProviderDTO = createMeetingProviderDTO(GOOGLE);
    meetingProviderDTO.setConferenceType(SINGLE);
    providerAccountManagerService.getSuitableAccount(START_DATE,END_DATE,meetingProviderDTO);
    verify(repository,times(1)).findByMeetingProviderId(anyString());
  }

  @Test
  public void findAccountByProviderId_itShouldThrowNotFoundExceptionWhenNotFound() {
    Mockito.lenient()
            .when(repository.findByMeetingProviderId(PROVIDER_ID))
            .thenReturn(Optional.empty());
    Assert.assertThrows(
            NotFoundException.class, () -> providerAccountManagerService.findAccountByProviderId(PROVIDER_ID));
  }
  @Test
  public void findAccountsByIds_itShouldThrowNotFoundExceptionWhenNotFound() {
    Mockito.lenient()
            .when(repository.findAllByIdIn(any()))
            .thenReturn(Optional.empty());
    Assert.assertThrows(
        NotFoundException.class, () -> providerAccountManagerService.findAccountsByIds(any()));
  }

}
