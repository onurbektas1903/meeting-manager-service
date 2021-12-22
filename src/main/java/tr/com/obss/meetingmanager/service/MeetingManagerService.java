package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.common.exceptions.BusinessValidationException;
import tr.com.common.exceptions.NotFoundException;
import tr.com.obss.meetingmanager.dto.IMeetingOrganizerReportDTO;
import tr.com.obss.meetingmanager.dto.IMeetingTimeReportDTO;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingQueryDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.SlotRequest;
import tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum;
import tr.com.obss.meetingmanager.exception.MeetingOccupiedException;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.mapper.SlotRequestMapper;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapper;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingManagerService {
  private final MeetingRepository repository;
  private final MeetingMapper mapper;
  private final MeetHandlerFactory handlerFactory;
  private final SlotRequestMapper slotRequestMapper;
  private final SlotRequestRepository slotRepository;
  private final GoogleMeetingService googleMeetingService;
  private final ProviderManagerService providerManagerService;
  private final KafkaMessageSender messageSender;
  @Value("${topics.notification}")
  private String notificationTopic;

  @Transactional("ptm")
  public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
    validate(meetingDTO);
    MeetingProvider provider = providerManagerService.getById(meetingDTO.getMeetingProvider().getId());
    meetingDTO.setProviderAccount(getSuitableAccount(meetingDTO.getStart(),meetingDTO.getEnd(),provider));
    MeetingDTO createdMeeting =
        handlerFactory
            .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
            .handleCreate(meetingDTO,provider.getSettings());
    Meeting meeting = mapper.toEntity(createdMeeting);
    repository.save(meeting);
    MeetingDTO createdDTO = mapper.toDTO(meeting);
    messageSender.sendCreated(notificationTopic,createdDTO);
    log.info("Meeting successfully created",createdMeeting.getId());
    return createdDTO;
  }
//TODO eğer o slot tarihinde boşta hesap yoksa oluşturamasın
  @Transactional("ptm")
  public SlotRequestDTO addSlotRequestToMeeting(SlotRequestDTO slotRequestDTO) {
    Meeting meeting = findById(slotRequestDTO.getMeetingId());
    validateSlotRequest(slotRequestDTO, meeting);
    googleMeetingService.sendChangeSlotMail(slotRequestDTO);
    SlotRequest slotRequestEntity = slotRequestMapper.toEntity(slotRequestDTO);
    slotRequestEntity.setRequestStatus(SlotRequestStatusEnum.WAITING);
    slotRequestEntity.setId(UUID.randomUUID().toString());
    meeting.addSlotRequest(slotRequestEntity);
    return slotRequestMapper.toDTO(slotRequestEntity);
  }

  public List<SlotRequestDTO> getSlotRequests(String meetingId){
     return slotRequestMapper.toDTOList(slotRepository.findAllByMeetingId(meetingId));
  }

  public List<MeetingDTO> searchMeetings(MeetingQueryDTO queryDTO) {
    return mapper.toDTOList(repository.searchMeetings(queryDTO));
  }

  @Transactional("ptm")
  public MeetingDTO updateMeeting(MeetingDTO meetingDTO, String id) {
    validate(meetingDTO);
    Meeting meeting = findById(id);
    if(isDateChanged(meetingDTO.getStart(),meetingDTO.getEnd(),meeting.getStartDate(),meeting.getEndDate())){
      checkIsAccountFreeInGivenRange(meetingDTO.getProviderAccount(),meetingDTO.getStart(),meetingDTO.getEnd());
    }
    MeetingProvider meetingProvider = providerManagerService.getById(meeting.getMeetingProvider().getId());
    MeetingDTO updatedMeeting =
        handlerFactory
            .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
            .handleUpdate(meetingDTO,meetingProvider.getSettings());
    mapper.updateMeeting(updatedMeeting, meeting);
    Meeting created = repository.save(meeting);
    log.info("Meeting Sucessfully Updated",created.getId());
    return mapper.toDTO(created);
  }

  @Transactional("ptm")
  public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
    Meeting meeting = findById(slotRequestDTO.getMeetingId());

    SlotRequest slotRequest = findSlotRequestById(slotRequestDTO.getId());
    checkIsAccountFreeInGivenRange(meeting.getProviderAccount(),slotRequest.getStartDate(),slotRequest.getEndDate());
    validateSlotRequest(slotRequestDTO, meeting);
    if (isApproved) {
      meeting.setStartDate(slotRequestDTO.getStartDate());
      meeting.setEndDate(slotRequestDTO.getEndDate());
      MeetingDTO meetingDTO = mapper.toDTO(meeting);
      handlerFactory
          .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
          .handleUpdate(meetingDTO,meeting.getMeetingProvider().getSettings());
      updateSlotRequest(slotRequest, SlotRequestStatusEnum.APPROVED);
      repository.save(meeting);
      messageSender.sendUpdated(notificationTopic,meetingDTO);
    } else {
      updateSlotRequest(slotRequest, SlotRequestStatusEnum.REJECTED);
    }
    log.info("Slot Request Sucessfully Handled",slotRequest.getId());
    return slotRequestMapper.toDTO(slotRequest);
  }
  private boolean isDateChanged(long start, long end, long oldStart,long oldEnd){
    return !(start == oldStart && end == oldEnd);
  }

  public List<IMeetingTimeReportDTO> getTimeBasedUsageReport(long startDate, long endDate){
    return repository.findUsageStatistics(startDate,endDate);
  }
  public List<IMeetingOrganizerReportDTO> getOrganizerReport(long startDate, long endDate){
    return repository.findTopOrganizers(startDate,endDate);
  }
  public List<MeetingDTO> getMeetingsByProviderIdBtwRange(String providerId,long startDate, long endDate){
    return mapper.toDTOList(repository.findAllByMeetingProviderIdAndStartDateBetween(providerId,startDate,endDate));
  }
  public void checkIsAccountFreeInGivenRange(String accountId, long startDate, long endDate){
    List<Meeting> accountMeetings = repository.findAllByProviderAccountAndStartDateBetween(accountId, startDate,endDate);
    if(!accountMeetings.isEmpty()){
      throw new MeetingOccupiedException(
              "No Free Accounts Found ", Collections.singleton("meetingAccount"));
    }
  }
  public String getSuitableAccount(long startDate, long endDate, MeetingProvider provider) {
    return provider.getConferenceType() == POOL
        ? findFreeAccountsForGivenDateRange(startDate, endDate, provider)
        : findProvidersAccount(provider);
  }

  private String findProvidersAccount(MeetingProvider provider) {
    return provider.getAccounts().keySet().stream()
        .findAny()
        .orElseThrow(() -> new NotFoundException("Meeting " + "Provider Not Found"));
  }

  private String findFreeAccountsForGivenDateRange(
      long startDate, long endDate, MeetingProvider provider) {
    Set<String> allAccounts = new HashSet<>(provider.getAccounts().keySet());

    List<String> usedAccounts =
        repository.findOccupiedAccounts(startDate, endDate, allAccounts);
    allAccounts.removeAll(usedAccounts);

    return allAccounts.stream().findAny().orElseThrow(()-> new MeetingOccupiedException(
            "No Free Accounts Found ", Collections.singleton("meetingAccount")));
  }

  private SlotRequest findSlotRequestById(String id) {
    return slotRepository
        .findById(id)
        .orElseThrow(
            () -> new NotFoundException("Slot Not Found", Collections.singleton("SlotRequest")));
  }

  @Transactional("ptm")
  public void updateSlotRequest(SlotRequest slotRequest, SlotRequestStatusEnum slotRequestStatus) {
    slotRequest.setRequestStatus(slotRequestStatus);
    slotRepository.save(slotRequest);
  }

  @Transactional("ptm")
  public SlotRequestDTO removeSlotRequest(String id) {
    // TODO send request canceled deleted mail
    SlotRequest slotRequest = findSlotRequestById(id);
    slotRepository.deleteById(slotRequest.getId());
    return slotRequestMapper.toDTO(slotRequest);
  }

  @Transactional("ptm")
  public void deleteMeeting(String id) {
    Meeting meeting = findById(id);
    MeetingDTO meetingDTO = mapper.toDTO(meeting);
    handlerFactory
        .findStrategy(meeting.getMeetingProvider().getMeetingProviderType())
        .handleCancel(meetingDTO);
    repository.deleteById(id);
    messageSender.sendDeleted(notificationTopic,meetingDTO);
  }

  public List<MeetingDTO> listMeetings(long start, long end) {
    return mapper.toDTOList(repository.findMeetingsBetweenStartAndEndDate(start, end));
  }

  private Meeting findById(String id) {
    return repository
        .findMeetingById(id)
        .orElseThrow(() -> new NotFoundException("Meeting Not Found"));
  }

  public MeetingDTO findMeetingById(String id) {
    return mapper.toDTO(findById(id));
  }

  private void validate(MeetingDTO meetingDTO) {
    if (meetingDTO.getStart() >= meetingDTO.getEnd()) {
      throw new BusinessValidationException(
          "Start time cant be bigger equal than end", Collections.singleton("startEnd"));
    }
    long now = Instant.now().toEpochMilli();
    if (meetingDTO.getStart() < now || meetingDTO.getEnd() < now) {
      throw new BusinessValidationException(
          "Start/End time cant be smaller than now", Collections.singleton("statEndNow"));
    }
  }

  private void validateSlotRequest(SlotRequestDTO slotRequestDTO, Meeting meeting) {
    long now = Instant.now().toEpochMilli();

    if (slotRequestDTO.getStartDate() == meeting.getStartDate()
        && slotRequestDTO.getEndDate() == meeting.getEndDate()) {
      throw new BusinessValidationException(
          "Slot request time must be diffrent from meeting",
          Collections.singletonMap("slotTime", "shouldBeDiffrent"));
    }
    if (slotRequestDTO.getStartDate() < now
        || slotRequestDTO.getEndDate() < now
        || slotRequestDTO.getStartDate() >= slotRequestDTO.getEndDate()) {

      throw new BusinessValidationException(
          "Start/End time cant be smaller than now",
          Collections.singletonMap("startEndDate", "invalidRange"));
    }
  }
}
