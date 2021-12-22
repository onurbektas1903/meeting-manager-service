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
import tr.com.common.exceptions.BusinessValidationException;
import tr.com.common.exceptions.ObjectInUseException;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.factory.MeetProviderHandlerFactory;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapper;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl_;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.google.GoogleProviderService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.PROVIDER_ID;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProvider;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderDTOSingle;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderSingle;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingEntity;
import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;
import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.SINGLE;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ProviderManagerServiceTest {

  private ProviderManagerService providerManagerService;
  private @Mock MeetProviderHandlerFactory handlerFactory;
  private @Mock MeetingProviderMapper providerMapper;
  private @Mock MeetingProviderRepository repository;
  private @Mock MeetingRepository meetingRepository;
  private @Mock GoogleProviderService googleProviderService;

  @BeforeEach
  public void init() {
    MeetingProviderMapperImpl_ mapperImpl = new MeetingProviderMapperImpl_();
    MeetingProviderMapperImpl impl = new MeetingProviderMapperImpl();
    impl.setDelegate(mapperImpl);
    providerManagerService =
        new ProviderManagerService(handlerFactory, impl, repository, meetingRepository);
    Mockito.lenient().when(repository.findById(PROVIDER_ID)).thenReturn(Optional.of(createMeetingProviderSingle(GOOGLE)));
  }

  @Test
  public void saveMeetingProvider_itShouldSuccessfullySave() {
    Mockito.lenient().when(handlerFactory.findStrategy(any())).thenReturn(googleProviderService);
    MeetingProviderDTO meetingProviderDTO = createMeetingProviderDTOSingle(GOOGLE);
    Mockito.lenient().when(googleProviderService.validateConferenceSettings(any())).thenReturn(meetingProviderDTO);
    Mockito.lenient().when(repository.save(any())).thenReturn(createMeetingProviderSingle(GOOGLE));
    MeetingProviderDTO newMeetingProviderDTO = providerManagerService.saveMeetingProvider(meetingProviderDTO);
    assertEquals(newMeetingProviderDTO.getId(), meetingProviderDTO.getId());
    assertEquals(newMeetingProviderDTO.getName(), meetingProviderDTO.getName());
    assertEquals(newMeetingProviderDTO.getConferenceType(), meetingProviderDTO.getConferenceType());
    assertEquals(newMeetingProviderDTO.getIsActive(), meetingProviderDTO.getIsActive());
    assertEquals(newMeetingProviderDTO.getUserRoleGroup(), meetingProviderDTO.getUserRoleGroup());
    assertEquals(newMeetingProviderDTO.getMeetingProviderType(), meetingProviderDTO.getMeetingProviderType());
    assertEquals(newMeetingProviderDTO.getAccounts(), meetingProviderDTO.getAccounts());
  }

  @Test
  public void findById_itShouldReturnSameMeetingProvider() {
    MeetingProvider meetingProvider = createMeetingProviderSingle(GOOGLE);
    MeetingProviderDTO newMeetingProviderDTO = providerManagerService.findById(PROVIDER_ID);
    assertEquals(newMeetingProviderDTO.getId(), meetingProvider.getId());
    assertEquals(newMeetingProviderDTO.getName(), meetingProvider.getName());
    assertEquals(newMeetingProviderDTO.getConferenceType(), meetingProvider.getConferenceType());
    assertEquals(newMeetingProviderDTO.getIsActive(), meetingProvider.getIsActive());
    assertEquals(newMeetingProviderDTO.getUserRoleGroup(), meetingProvider.getUserRoleGroup());
    assertEquals(newMeetingProviderDTO.getMeetingProviderType(), meetingProvider.getMeetingProviderType());
//    assertEquals(newMeetingProviderDTO.getSettings(), meetingProvider.getSettings());
    assertEquals(newMeetingProviderDTO.getAccounts(), meetingProvider.getAccounts());
  }

  @Test
  public void updateMeetingProvider_itShouldSuccessfullyUpdate() {
    Mockito.lenient().when(handlerFactory.findStrategy(any())).thenReturn(googleProviderService);
    MeetingProviderDTO meetingProviderDTO = createMeetingProviderDTOSingle(GOOGLE);
    meetingProviderDTO.setName("newName");
    meetingProviderDTO.setConferenceType(POOL);
    Mockito.lenient().when(googleProviderService.validateConferenceSettings(any())).thenReturn(meetingProviderDTO);
    MeetingProvider meetingProvider = createMeetingProviderSingle(GOOGLE);
    Mockito.lenient().when(repository.findById(PROVIDER_ID)).thenReturn(Optional.of(meetingProvider));
    Mockito.lenient().when(repository.save(meetingProvider)).thenReturn(meetingProvider);
    MeetingProviderDTO newMeetingProviderDTO = providerManagerService.updateMeetingProvider(meetingProviderDTO, PROVIDER_ID);
    assertEquals(newMeetingProviderDTO.getId(), meetingProviderDTO.getId());
    assertEquals(newMeetingProviderDTO.getName(), meetingProviderDTO.getName());
    assertEquals(newMeetingProviderDTO.getConferenceType(), meetingProviderDTO.getConferenceType());
  }

  @Test
  public void deleteMeetingProvider_itShouldSuccessfullyDelete() {
    Mockito.lenient()
        .when(meetingRepository.findByMeetingProviderId(PROVIDER_ID))
        .thenReturn(Collections.emptyList());
    providerManagerService.deleteMeetingProvider(PROVIDER_ID);
  }

  @Test
  public void deleteMeetingProvider_itShouldThrowObjectInUseExceptionForMeetingInUse() {
    Mockito.lenient()
        .when(meetingRepository.findByMeetingProviderId(PROVIDER_ID))
        .thenReturn(Arrays.asList(createMeetingEntity(), createMeetingEntity()));
    Assert.assertThrows(
            ObjectInUseException.class, () -> providerManagerService.deleteMeetingProvider(PROVIDER_ID));
  }

  @Test
  public void getMeetingProviders_itShouldSuccessfullyGetAllMeetingProviders() {
    Mockito.lenient()
            .when(repository.findAll())
            .thenReturn(Arrays.asList(createMeetingProvider(GOOGLE, SINGLE), createMeetingProvider(GOOGLE, SINGLE)));
    List<MeetingProviderDTO> activeProviders = providerManagerService.getMeetingProviders();
    assertEquals(2, activeProviders.size());
  }

  @Test
  public void getActiveProviders_itShouldSuccessfullyFindAllActiveProviders() {
    Mockito.lenient()
            .when(repository.findSuitableProviders(SINGLE,Collections.singleton("role")))
            .thenReturn(Arrays.asList(createMeetingProvider(GOOGLE, SINGLE), createMeetingProvider(GOOGLE, SINGLE)));
      List<MeetingProviderDTO> providers =
              providerManagerService.getActiveProviders(Collections.singleton("role"));
      assertEquals(2, providers.size());
  }

  @Test
  public void activateDeactivateProvider_itShouldSetIsActiveToGivenParameter() {
    MeetingProvider meetingProvider2 = createMeetingProviderSingle(GOOGLE);
    meetingProvider2.setIsActive(true);
    Mockito.lenient().when(repository.save(any())).thenReturn(meetingProvider2);
    MeetingProviderDTO newProviderDTO = providerManagerService.activateDeactivateProvider(PROVIDER_ID, true);
    assertEquals(newProviderDTO.getIsActive(), meetingProvider2.getIsActive());

    MeetingProvider meetingProvider3 = createMeetingProviderSingle(GOOGLE);
    meetingProvider2.setIsActive(false);
    Mockito.lenient().when(repository.save(any())).thenReturn(meetingProvider3);
    MeetingProviderDTO new2ProviderDTO = providerManagerService.activateDeactivateProvider(PROVIDER_ID, false);
    assertEquals(new2ProviderDTO.getIsActive(), meetingProvider3.getIsActive());
  }

}
