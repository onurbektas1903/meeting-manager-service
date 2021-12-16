package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingQueryDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.SlotRequest;
import tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum;
import tr.com.obss.meetingmanager.exception.BusinessValidationException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.mapper.SlotRequestMapper;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapper;
import tr.com.obss.meetingmanager.repository.MeetingRepository;
import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MeetingManagerService {
  private final MeetingRepository repository;
  private final MeetingMapper mapper;
  private final MeetHandlerFactory handlerFactory;
  private final SlotRequestMapper slotRequestMapper;
  private final SlotRequestRepository slotRepository;
  private final GoogleMeetingService googleMeetingService;
  @Transactional("ptm")
  public void createMeeting(MeetingDTO meetingDTO) {
    validate(meetingDTO);
    MeetingDTO createdMeeting =
        handlerFactory
            .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
            .handleCreate(meetingDTO);
    Meeting meeting = mapper.toEntity(meetingDTO);
     repository.save(meeting);
  }

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
  public List<MeetingDTO> searchMeetings(MeetingQueryDTO queryDTO){
    return mapper.toDTOList(repository.searchMeetings(queryDTO));
  }
  @Transactional("ptm")
  public MeetingDTO updateMeeting(MeetingDTO meetingDTO, String id) {
    validate(meetingDTO);
    Meeting meeting =findById(id);
    meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(meeting.getProviderAccount().getId()).build());
    MeetingDTO updatedMeeting =
        handlerFactory
            .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
            .handleUpdate(meetingDTO);
    mapper.updateMeeting(updatedMeeting, meeting);
    return mapper.toDTO(repository.save(meeting));
  }

  @Transactional("ptm")
  public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
    Meeting meeting = findById(slotRequestDTO.getMeetingId());
    SlotRequest slotRequest = findSlotRequestById(slotRequestDTO.getId());
    validateSlotRequest(slotRequestDTO, meeting);
    if (isApproved) {
      meeting.setStartDate(slotRequestDTO.getStartDate());
      meeting.setEndDate(slotRequestDTO.getEndDate());
      MeetingDTO meetingDTO = mapper.toDTOWithAccount(meeting);
      handlerFactory
          .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
          .handleUpdate(meetingDTO);
      updateSlotRequest(slotRequest, SlotRequestStatusEnum.APPROVED);
      repository.save(meeting);
    } else {
        updateSlotRequest(slotRequest, SlotRequestStatusEnum.REJECTED);
    }
    return slotRequestMapper.toDTO(slotRequest);
  }

  private SlotRequest findSlotRequestById(String id) {
    return slotRepository
        .findById(id)
        .orElseThrow(
            () -> new NotFoundException("Slot Not Found", Collections.singleton("SlotRequest")));
  }

  @Transactional("ptm")
  public void updateSlotRequest(SlotRequest slotRequest,SlotRequestStatusEnum slotRequestStatus) {
    //TODO başlangıç bitişi uppdate et
      slotRequest.setRequestStatus(slotRequestStatus);
      slotRepository.save(slotRequest);
  }
  @Transactional("ptm")
  public SlotRequestDTO removeSlotRequest(String id){
    //TODO send deleted mail
    SlotRequest slotRequest = findSlotRequestById(id);
    slotRepository.deleteById(slotRequest.getId());
    return slotRequestMapper.toDTO(slotRequest);
  }
  @Transactional("ptm")
  public void deleteMeeting(String id) {
    Meeting meeting = findById(id);
    handlerFactory.findStrategy(meeting.getProviderAccount().getMeetingProviderType()).handleCancel(mapper.toDTOWithAccount(meeting));
    repository.deleteById(id);
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
          "Slot request time must be diffrent from meeting",  Collections.singletonMap("slotTime","shouldBeDiffrent"));
    }
    if (slotRequestDTO.getStartDate() < now || slotRequestDTO.getEndDate() < now) {

      throw new BusinessValidationException(
          "Start/End time cant be smaller than now", Collections.singletonMap("startEndDate","invalidRange"));
    }
  }
}
