package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.SlotRequest;
import tr.com.obss.meetingmanager.exception.BusinessValidationException;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.mapper.SlotRequestMapper;
import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.APPROVED;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.REJECTED;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotRequestService {

  private final GoogleMeetingService googleMeetingService;
  private final MeetingManagerService meetingManager;
  private final SlotRequestRepository repository;
  private final SlotRequestMapper mapper;
  private final MeetHandlerFactory handlerFactory;

  @Transactional
  public SlotRequestDTO createChangeSlotRequest(SlotRequestDTO slotRequestDTO) {
            googleMeetingService.sendChangeSlotMail(slotRequestDTO);
    MeetingDTO meetingDTO = meetingManager.findMeetingById(slotRequestDTO.getMeetingId());
    validateSlotRequest(slotRequestDTO,meetingDTO);

    SlotRequest slotRequest = mapper.toEntity(slotRequestDTO);
    slotRequest.setId(UUID.randomUUID().toString());
    SlotRequest saved = repository.save(slotRequest);
    log.info("Slot created for meeting: ", meetingDTO);
    return mapper.toDTO(saved);
  }

  public List<SlotRequestDTO> getSlotRequestsByMeetingId(String meetingId) {
    return mapper.toDTOList(repository.findAllByMeetingId(meetingId));
  }

  @Transactional
  public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
    if (isApproved) {
      MeetingDTO meetingDTO = meetingManager.findMeetingById(slotRequestDTO.getMeetingId());
      validateSlotRequest(slotRequestDTO,meetingDTO);
      meetingDTO.setStart(slotRequestDTO.getStartDate());
      meetingDTO.setEnd(slotRequestDTO.getEndDate());

      slotRequestDTO.setRequestStatus(APPROVED);
      handlerFactory
          .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
          .handleUpdate(meetingDTO);
      return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
    } else {
      slotRequestDTO.setRequestStatus(REJECTED);
      return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
    }
  }

  private void validateSlotRequest(SlotRequestDTO slotRequestDTO, MeetingDTO meetingDTO) {
    long now = Instant.now().toEpochMilli();
    if (slotRequestDTO.getStartDate() == meetingDTO.getStart()
        && slotRequestDTO.getEndDate() == meetingDTO.getEnd()){
      throw new BusinessValidationException(
              "Slot request time must be diffrent from meeting", Collections.singleton("slotTime"));
    }
      if (slotRequestDTO.getStartDate() < now || slotRequestDTO.getEndDate() < now) {
      throw new BusinessValidationException(
          "Start/End time cant be smaller than now", Collections.singleton("statEndNow"));
    }
  }
}
