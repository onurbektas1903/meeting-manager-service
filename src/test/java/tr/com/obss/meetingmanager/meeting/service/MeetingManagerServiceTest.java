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
import tr.com.common.exceptions.NotFoundException;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.SlotRequest;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.exception.MeetingOccupiedException;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.mapper.SlotRequestMapperImpl;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapperImpl;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapperImpl_;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl_;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anySet;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.APPROVED;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.REJECTED;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.WAITING;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderDTOSingle;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderPool;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.MEETING_ID;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.SLOT1_ID;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingDTO;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingDTOFromPoolProvider;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingEntity;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createSlotRequest;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createSlotRequestDTO;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class MeetingManagerServiceTest {

  private MeetingManagerService meetingManagerService;
  private @Mock MeetingRepository repository;
  private @Mock MeetHandlerFactory meetHandlerFactory;
  private @Mock SlotRequestRepository slotRepository;
  private @Mock GoogleMeetingService googleMeetingService;
  private @Mock ProviderManagerService providerManagerService;
  private @Mock KafkaMessageSender messageSender;

  @BeforeEach
  public void init() {
    MeetingMapperImpl_ mapperImpl = new MeetingMapperImpl_();
    MeetingMapperImpl impl = new MeetingMapperImpl();
    MeetingProviderMapperImpl providerMapper = new MeetingProviderMapperImpl();
    MeetingProviderMapperImpl_ delegate = new MeetingProviderMapperImpl_();
    providerMapper.setDelegate(delegate);
    impl.setDelegate(mapperImpl);
    impl.setProviderMapper(providerMapper);
    meetingManagerService =
        new MeetingManagerService(
            repository,
            impl,
            meetHandlerFactory,
            new SlotRequestMapperImpl(),
            slotRepository,
            googleMeetingService,
            providerManagerService,messageSender);
    Mockito.lenient()
        .when(repository.findMeetingById(Mockito.anyString()))
        .thenReturn(Optional.of(createMeetingEntity()));
  }

  @Test
  public void createMeeting_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
    MeetingDTO meetingDTO = createMeetingDTO();
    meetingDTO.setEnd(Instant.now().toEpochMilli());
    Assert.assertThrows(
        BusinessValidationException.class, () -> meetingManagerService.createMeeting(meetingDTO));
  }

  @Test
  public void createMeeting_itShouldThrowBusinessValidationExceptionWhenStartIsLessThanNow() {
    MeetingDTO meetingDTO = createMeetingDTO();
    meetingDTO.setStart(Instant.now().minusSeconds(3).toEpochMilli());
    meetingDTO.setEnd(Instant.now().minusSeconds(2).toEpochMilli());
    Assert.assertThrows(
        BusinessValidationException.class, () -> meetingManagerService.createMeeting(meetingDTO));
  }

  @Test
  public void createMeeting_itShouldSuccessfullySavePool() {
    MeetingDTO meetingDTO = createMeetingDTOFromPoolProvider();
    MeetingProvider meetingProvider = createMeetingProviderPool(MeetingProviderTypeEnum.GOOGLE);
    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
    Mockito.lenient().when(googleMeetingService.handleCreate(any(),any())).thenReturn(meetingDTO);
    Mockito.lenient().when(providerManagerService.getById(any())).thenReturn(meetingProvider);
    Mockito.lenient()
            .when(repository.findOccupiedAccounts(anyLong(), anyLong(), anySet()))
            .thenReturn(new ArrayList<>());
  }

  @Test
  public void createMeeting_itShouldThrowMeetingOccupiedExceptionWhenNoFreeAccountAtGivenDate() {
    MeetingDTO meetingDTO = createMeetingDTOFromPoolProvider();
    MeetingProvider meetingProvider = createMeetingProviderPool(MeetingProviderTypeEnum.GOOGLE);
    meetingProvider.setAccounts(new HashMap<>());
    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
    Mockito.lenient().when(googleMeetingService.handleCreate(any(),any())).thenReturn(meetingDTO);
    Mockito.lenient().when(providerManagerService.getById(any())).thenReturn(meetingProvider);
    Mockito.lenient()
            .when(repository.findOccupiedAccounts(anyLong(), anyLong(), anySet()))
            .thenReturn(Collections.singletonList("accountId"));
    Assert.assertThrows(
        MeetingOccupiedException.class, () -> meetingManagerService.createMeeting(meetingDTO));
  }
  @Test
  public void updateMeeting_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
    MeetingDTO meetingDTO = createMeetingDTO();
    meetingDTO.setEnd(Instant.now().toEpochMilli());
    Assert.assertThrows(
        BusinessValidationException.class,
        () -> meetingManagerService.updateMeeting(meetingDTO, MEETING_ID));
  }

  @Test
  public void updateMeeting_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
    MeetingDTO meetingDTO = createMeetingDTO();
    Mockito.lenient()
        .when(repository.findMeetingById(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Assert.assertThrows(
        NotFoundException.class, () -> meetingManagerService.updateMeeting(meetingDTO, MEETING_ID));
  }

  @Test
  public void deleteMeeting_itShouldSucessfullyDelete() {
    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
    meetingManagerService.deleteMeeting(MEETING_ID);
  }

  @Test
  public void deleteMeeting_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
    Mockito.lenient().when(repository.findMeetingById(MEETING_ID)).thenReturn(Optional.empty());
    Assert.assertThrows(
        NotFoundException.class, () -> meetingManagerService.deleteMeeting(MEETING_ID));
  }

  @Test
  public void getTimeBasedUsageReport_itShouldReturnTimeBasedUsageReport() {
    long start = Instant.now().toEpochMilli();
    long end = Instant.now().plusSeconds(1).toEpochMilli();
    Mockito.lenient()
        .when(repository.findUsageStatistics(start, end))
        .thenReturn(Arrays.asList(anyObject(), anyObject()));
    assertEquals(2, meetingManagerService.getTimeBasedUsageReport(start, end).size());
  }

  @Test
  public void getOrganizerReport_itShouldReturnOrganizerReport() {
    long start = Instant.now().toEpochMilli();
    long end = Instant.now().plusSeconds(1).toEpochMilli();
    Mockito.lenient()
        .when(repository.findTopOrganizers(start, end))
        .thenReturn(Arrays.asList(anyObject(), anyObject()));
    assertEquals(2, meetingManagerService.getOrganizerReport(start, end).size());
  }

  @Test
  public void addSlotRequestToMeeting_itShouldSucessfullyAdd() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    slotRequestDTO.setRequestStatus(null);
    SlotRequestDTO response = meetingManagerService.addSlotRequestToMeeting(slotRequestDTO);
    assertEquals(response.getCreator(), slotRequestDTO.getCreator());
    assertEquals(response.getOrganizer(), slotRequestDTO.getOrganizer());
    assertEquals(response.getStartDate(), slotRequestDTO.getStartDate());
    assertEquals(response.getEndDate(), slotRequestDTO.getEndDate());
    assertEquals(response.getRequestStatus(), WAITING);
  }

  @Test
  public void addSlotRequestToMeeting_itShouldThrowBusinessValidationExceptionWhenSlotAndMeetingTimesDoesNotMatch() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    slotRequestDTO.setRequestStatus(null);
    Meeting meeting = createMeetingEntity();
    long start = Instant.now().toEpochMilli();
    long end = Instant.now().plusSeconds(1).toEpochMilli();
    slotRequestDTO.setStartDate(start);
    slotRequestDTO.setEndDate(end);
    meeting.setStartDate(start);
    meeting.setEndDate(end);
    Mockito.lenient().when(repository.findMeetingById(any())).thenReturn(Optional.of(meeting));
    Assert.assertThrows(
            BusinessValidationException.class,
            () -> meetingManagerService.addSlotRequestToMeeting(slotRequestDTO));
  }

  @Test
  public void addSlotRequest_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
    Mockito.lenient().when(repository.findMeetingById(MEETING_ID)).thenReturn(Optional.empty());
    Assert.assertThrows(
        NotFoundException.class,
        () -> meetingManagerService.addSlotRequestToMeeting(createSlotRequestDTO()));
  }

  @Test
  public void addSlotRequest_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    slotRequestDTO.setStartDate(Instant.now().plus(150, MINUTES).toEpochMilli());
    Assert.assertThrows(
        BusinessValidationException.class,
        () -> meetingManagerService.addSlotRequestToMeeting(slotRequestDTO));
  }

  @Test
  public void removeSlotRequest_itShouldSuccessfullyDelete() {
    SlotRequest slotRequest = createSlotRequest();
    Mockito.lenient().when(slotRepository.findById(SLOT1_ID)).thenReturn(Optional.of(slotRequest));
    meetingManagerService.removeSlotRequest(SLOT1_ID);
  }

  @Test
  public void handleRequestApproval_itShouldSuccessfullyReject() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createSlotRequest()));
    SlotRequestDTO response = meetingManagerService.handleRequestApproval(slotRequestDTO, false);
    assertEquals(response.getRequestStatus(), REJECTED);
    assertEquals(response.getStartDate(), slotRequestDTO.getStartDate());
    assertEquals(response.getEndDate(), slotRequestDTO.getEndDate());
    assertEquals(response.getTitle(), slotRequestDTO.getTitle());
  }

  @Test
  public void handleRequestApproval_itShouldSuccessfullyApprove() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createSlotRequest()));
    Mockito.lenient().when(googleMeetingService.handleUpdate(any(),any())).thenReturn(createMeetingDTO());
    SlotRequestDTO response = meetingManagerService.handleRequestApproval(slotRequestDTO, true);
    assertEquals(response.getRequestStatus(), APPROVED);
    assertEquals(response.getStartDate(), slotRequestDTO.getStartDate());
    assertEquals(response.getEndDate(), slotRequestDTO.getEndDate());
    assertEquals(response.getTitle(), slotRequestDTO.getTitle());
  }

  @Test
  public void handleRequestApproval_itShouldThrowNotFoundExceptionWhenSlotRequestIsNotFound() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.empty());
    Assert.assertThrows(
        NotFoundException.class,
        () -> meetingManagerService.handleRequestApproval(slotRequestDTO, true));
  }

  @Test
  public void
      handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createSlotRequest()));
    slotRequestDTO.setEndDate(Instant.now().toEpochMilli());
    Assert.assertThrows(
        BusinessValidationException.class,
        () -> meetingManagerService.handleRequestApproval(slotRequestDTO, true));
  }

  @Test
  public void
      handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenStartIsSmallerThanNow() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    slotRequestDTO.setStartDate(Instant.now().minus(1, MINUTES).toEpochMilli());
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createSlotRequest()));
    Assert.assertThrows(
        BusinessValidationException.class,
        () -> meetingManagerService.handleRequestApproval(slotRequestDTO, true));
  }

  @Test
  public void
      handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenEndIsSmallerThanNow() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    slotRequestDTO.setEndDate(Instant.now().minus(1, MINUTES).toEpochMilli());
    Mockito.lenient()
        .when(slotRepository.findById(Mockito.anyString()))
        .thenReturn(Optional.of(createSlotRequest()));
    Assert.assertThrows(
        BusinessValidationException.class,
        () -> meetingManagerService.handleRequestApproval(slotRequestDTO, true));
  }

  @Test
  public void findMeetingById_itShouldSuccessfullyFindMeetingById() {
    MeetingDTO meetingDTO = meetingManagerService.findMeetingById(MEETING_ID);
    assertEquals(MEETING_ID, meetingDTO.getId());
  }

  @Test
  public void getSlotRequests_itShouldSuccessfullyGetSlotRequests() {
    Mockito.lenient()
            .when(slotRepository.findAllByMeetingId(any()))
            .thenReturn(Collections.singletonList(createSlotRequest()));
    List<SlotRequestDTO> slotRequests = meetingManagerService.getSlotRequests(any());
    assertEquals(1, slotRequests.size());
  }

  @Test
  public void searchMeetings_itShouldSuccessfullySearchMeetings() {
    Mockito.lenient()
            .when(repository.searchMeetings(any()))
            .thenReturn(Collections.singletonList(createMeetingEntity()));
    List<MeetingDTO> meetingDTOList = meetingManagerService.searchMeetings(any());
    assertEquals(1, meetingDTOList.size());
  }

  @Test
  public void listMeetings_itShouldSuccessfullyListMeetings() {
    Mockito.lenient()
            .when(repository.findMeetingsBetweenStartAndEndDate(anyLong(), anyLong()))
            .thenReturn(Collections.singletonList(createMeetingEntity()));
    List<MeetingDTO> meetingDTOList = meetingManagerService.listMeetings(anyLong(), anyLong());
    assertEquals(1, meetingDTOList.size());
  }
}
