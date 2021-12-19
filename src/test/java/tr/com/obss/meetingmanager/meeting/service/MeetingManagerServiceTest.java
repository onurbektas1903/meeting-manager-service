//package tr.com.obss.meetingmanager.meeting.service;
//
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import tr.com.obss.meetingmanager.dto.MeetingDTO;
//import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
//import tr.com.obss.meetingmanager.entity.Meeting;
//import tr.com.obss.meetingmanager.exception.BusinessValidationException;
//import tr.com.obss.meetingmanager.exception.NotFoundException;
//import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
//import tr.com.obss.meetingmanager.mapper.SlotRequestMapperImpl;
//import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapperImpl;
//import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapperImpl_;
//import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl;
//import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapperImpl_;
//import tr.com.obss.meetingmanager.repository.MeetingRepository;
//import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
//import tr.com.obss.meetingmanager.service.MeetingManagerService;
//import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static java.time.temporal.ChronoUnit.MINUTES;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.APPROVED;
//import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.REJECTED;
//import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.WAITING;
//import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.MEETING_ID;
//import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingDTO;
//import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createMeetingEntity;
//import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createSlotRequest;
//import static tr.com.obss.meetingmanager.meeting.utils.MeetingTestObjectFactory.createSlotRequestDTO;
//
//@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
//public class MeetingManagerServiceTest {
//
//  private MeetingManagerService meetingManagerService;
//  private @Mock MeetingRepository repository;
//  private @Mock MeetHandlerFactory meetHandlerFactory;
//  private @Mock SlotRequestRepository slotRepository;
//  private @Mock GoogleMeetingService googleMeetingService;
//
//  @BeforeEach
//  public void init() {
//    MeetingMapperImpl_ mapperImpl = new MeetingMapperImpl_();
//    MeetingMapperImpl impl = new MeetingMapperImpl();
//    MeetingProviderMapperImpl providerMapper = new MeetingProviderMapperImpl();
//    MeetingProviderMapperImpl_ delegate = new MeetingProviderMapperImpl_();
//    providerMapper.setDelegate(delegate);
//    impl.setDelegate(mapperImpl);
//    impl.setProviderMapper(providerMapper);
//    meetingManagerService =
//        new MeetingManagerService(
//            repository,
//            impl,
//            meetHandlerFactory,
//            new SlotRequestMapperImpl(),
//            slotRepository,
//            googleMeetingService);
//    Mockito.lenient()
//        .when(repository.findMeetingById(Mockito.anyString()))
//        .thenReturn(Optional.of(createMeetingEntity()));
//  }
//
//  @Test
//  public void createMeeting_itShouldSucessfullySave() {
//    MeetingDTO meetingDTO = createMeetingDTO();
//    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
//    Mockito.lenient().when(googleMeetingService.handleCreate(meetingDTO)).thenReturn(meetingDTO);
//    meetingManagerService.createMeeting(meetingDTO);
//  }
//
//  @Test
//  public void createMeeting_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
//    MeetingDTO meetingDTO = createMeetingDTO();
//    meetingDTO.setEnd(Instant.now().toEpochMilli());
//    Assert.assertThrows(
//        BusinessValidationException.class, () -> meetingManagerService.createMeeting(meetingDTO));
//  }
//
//  @Test
//  public void updateMeeting_itShouldSucessfullyUpdate() {
//    MeetingDTO meetingDTO = createMeetingDTO();
//    Meeting meetingEntity = createMeetingEntity();
//    meetingDTO.setRecipients(null);
//    meetingEntity.setRecipients(new ArrayList<>());
//    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
//    Mockito.lenient().when(googleMeetingService.handleUpdate(meetingDTO)).thenReturn(meetingDTO);
//    Mockito.lenient().when(repository.save(any())).thenReturn(meetingEntity);
//
//    MeetingDTO result = meetingManagerService.updateMeeting(meetingDTO, MEETING_ID);
//    assertEquals(result.getId(), meetingDTO.getId());
//    assertEquals(result.getDescription(), meetingDTO.getDescription());
//    assertEquals(result.getStart(), meetingDTO.getStart());
//    assertEquals(result.getEnd(), meetingDTO.getEnd());
//  }
//
//  @Test
//  public void updateMeeting_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
//    MeetingDTO meetingDTO = createMeetingDTO();
//    meetingDTO.setEnd(Instant.now().toEpochMilli());
//    Assert.assertThrows(
//        BusinessValidationException.class,
//        () -> meetingManagerService.updateMeeting(meetingDTO, MEETING_ID));
//  }
//
//  @Test
//  public void updateMeeting_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
//    MeetingDTO meetingDTO = createMeetingDTO();
//    Mockito.lenient()
//        .when(repository.findMeetingById(Mockito.anyString()))
//        .thenReturn(Optional.empty());
//    Assert.assertThrows(
//        NotFoundException.class, () -> meetingManagerService.updateMeeting(meetingDTO, MEETING_ID));
//  }
//
//  @Test
//  public void deleteMeeting_itShouldSucessfullyDelete() {
//
//    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
//    meetingManagerService.deleteMeeting(MEETING_ID);
//  }
//
//  @Test
//  public void deleteMeeting_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
//    Mockito.lenient().when(repository.findMeetingById(MEETING_ID)).thenReturn(Optional.empty());
//    Assert.assertThrows(
//        NotFoundException.class, () -> meetingManagerService.deleteMeeting(MEETING_ID));
//  }
//
//  @Test
//  public void addSlotRequestToMeeting_itShouldSucessfullyAdd() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    slotRequestDTO.setRequestStatus(null);
//    SlotRequestDTO response = meetingManagerService.addSlotRequestToMeeting(slotRequestDTO);
//    assertEquals(response.getCreator(), slotRequestDTO.getCreator());
//    assertEquals(response.getOrganizer(), slotRequestDTO.getOrganizer());
//    assertEquals(response.getStartDate(), slotRequestDTO.getStartDate());
//    assertEquals(response.getEndDate(), slotRequestDTO.getEndDate());
//    assertEquals(response.getRequestStatus(), WAITING);
//  }
//
//  @Test
//  public void addSlotRequest_itShouldThrowNotFoundExceptionWhenMeetingNotFound() {
//    Mockito.lenient().when(repository.findMeetingById(MEETING_ID)).thenReturn(Optional.empty());
//    Assert.assertThrows(
//        NotFoundException.class,
//        () -> meetingManagerService.addSlotRequestToMeeting(createSlotRequestDTO()));
//  }
//
//  @Test
//  public void addSlotRequest_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    slotRequestDTO.setStartDate(Instant.now().plus(150,MINUTES).toEpochMilli());
//    Assert.assertThrows(
//        BusinessValidationException.class,
//        () -> meetingManagerService.addSlotRequestToMeeting(slotRequestDTO));
//  }
//
//  @Test
//  public void handleRequestApproval_itShouldSuccessfullyReject() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    Mockito.lenient()
//        .when(slotRepository.findById(Mockito.anyString()))
//        .thenReturn(Optional.of(createSlotRequest()));
//    SlotRequestDTO response = meetingManagerService.handleRequestApproval(slotRequestDTO, false);
//    assertEquals(response.getRequestStatus(),REJECTED);
//    assertEquals(response.getStartDate(),slotRequestDTO.getStartDate());
//    assertEquals(response.getEndDate(),slotRequestDTO.getEndDate());
//    assertEquals(response.getTitle(),slotRequestDTO.getTitle());
//  }
//  @Test
//  public void handleRequestApproval_itShouldSuccessfullyApprove() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    Mockito.lenient().when(meetHandlerFactory.findStrategy(any())).thenReturn(googleMeetingService);
//    Mockito.lenient()
//        .when(slotRepository.findById(Mockito.anyString()))
//        .thenReturn(Optional.of(createSlotRequest()));
//    Mockito.lenient().when(googleMeetingService.handleUpdate(any())).thenReturn(createMeetingDTO());
//    SlotRequestDTO response = meetingManagerService.handleRequestApproval(slotRequestDTO, true);
//    assertEquals(response.getRequestStatus(),APPROVED);
//    assertEquals(response.getStartDate(),slotRequestDTO.getStartDate());
//    assertEquals(response.getEndDate(),slotRequestDTO.getEndDate());
//    assertEquals(response.getTitle(),slotRequestDTO.getTitle());
//  }
//
//  @Test
//  public void handleRequestApproval_itShouldThrowNotFoundExceptionWhenSlotRequestIsNotFound() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    Mockito.lenient()
//            .when(slotRepository.findById(Mockito.anyString()))
//            .thenReturn(Optional.empty());
//    Assert.assertThrows(
//            NotFoundException.class,
//            () -> meetingManagerService.handleRequestApproval(slotRequestDTO,true));
//  }
//
//  @Test
//  public void handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenStartIsBiggerThanEnd() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    Mockito.lenient()
//            .when(slotRepository.findById(Mockito.anyString()))
//            .thenReturn(Optional.of(createSlotRequest()));
//    slotRequestDTO.setEndDate(Instant.now().toEpochMilli());
//    Assert.assertThrows(
//            BusinessValidationException.class,
//            () -> meetingManagerService.handleRequestApproval(slotRequestDTO,true));
//  }
//  @Test
//  public void handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenStartIsSmallerThanNow() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    slotRequestDTO.setStartDate(Instant.now().minus(1,MINUTES).toEpochMilli());
//    Mockito.lenient()
//            .when(slotRepository.findById(Mockito.anyString()))
//            .thenReturn(Optional.of(createSlotRequest()));
//    Assert.assertThrows(
//            BusinessValidationException.class,
//            () -> meetingManagerService.handleRequestApproval(slotRequestDTO,true));
//  }
//  @Test
//  public void handleRequestApproval_itShouldThrowBusinessValidationExceptionWhenEndIsSmallerThanNow() {
//    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
//    slotRequestDTO.setEndDate(Instant.now().minus(1,MINUTES).toEpochMilli());
//    Mockito.lenient()
//            .when(slotRepository.findById(Mockito.anyString()))
//            .thenReturn(Optional.of(createSlotRequest()));
//    Assert.assertThrows(
//            BusinessValidationException.class,
//            () -> meetingManagerService.handleRequestApproval(slotRequestDTO,true));
//  }
//
//}
