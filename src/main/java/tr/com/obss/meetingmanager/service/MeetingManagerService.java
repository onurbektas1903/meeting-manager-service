package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.exception.BusinessValidationException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapper;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.APPROVED;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.REJECTED;


@Service
@RequiredArgsConstructor
public class MeetingManagerService  {
    private final MeetingRepository repository;
    private final MeetingMapper mapper;
    private final MeetHandlerFactory handlerFactory;

    @Transactional
    public MeetingDTO createMeeting(MeetingDTO meetingDTO){
        validate(meetingDTO);
        MeetingDTO createdMeeting =
                handlerFactory.findStrategy(meetingDTO.getMeetingProviderType()).handleCreate(meetingDTO);
       return saveMeeting(createdMeeting);
    }

    @Transactional
    public MeetingDTO saveMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = mapper.toEntity(meetingDTO);
        return mapper.toDTO(repository.save(meeting));
    }
    @Transactional
    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
        validate(meetingDTO);
        Meeting meeting = repository.findMeetingById(meetingDTO.getId()).orElseThrow(() -> new NotFoundException(
                "Meeting Not Found"));
        MeetingDTO updatedMeeting =
                handlerFactory.findStrategy(meetingDTO.getMeetingProviderType()).handleUpdate(meetingDTO);
        mapper.updateMeeting(updatedMeeting,meeting);
        return mapper.toDTO(repository.save(meeting)) ;
    }
    public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
        if (isApproved) {
            Meeting meeting = findById(slotRequestDTO.getMeetingId());
            MeetingDTO meetingDTO = mapper.toDTO(meeting);
            validateSlotRequest(slotRequestDTO,meetingDTO);
            meetingDTO.setStart(slotRequestDTO.getStartDate());
            meetingDTO.setEnd(slotRequestDTO.getEndDate());
            handlerFactory
                    .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
                    .handleUpdate(meetingDTO);
            slotRequestDTO.setRequestStatus(APPROVED);
            return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
        } else {
            slotRequestDTO.setRequestStatus(REJECTED);
            return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
        }
    }

    public MeetingDTO deleteMeeting(MeetingDTO meetingDTO) {
        return null;
    }
    public List<MeetingDTO> listMeetings(long start,long end) {

        return mapper.toDTOList(repository.findMeetingsBetweenStartAndEndDate(start,end));
    }
    private Meeting findById(String id){
        return repository.findMeetingById(id).orElseThrow(()-> new NotFoundException(
                "Meeting Not Found"));
    }
    public MeetingDTO findMeetingById(String id ){
        return mapper.toDTO(findById(id));
    }
    private void validate(MeetingDTO meetingDTO){
        if(meetingDTO.getStart() >= meetingDTO.getEnd()){
            throw new BusinessValidationException("Start time cant be bigger equal than end", Collections.singleton(
                    "startEnd"));
        }
        long now = Instant.now().toEpochMilli();
        if(meetingDTO.getStart() < now || meetingDTO.getEnd() < now ){
            throw new BusinessValidationException("Start/End time cant be smaller than now",
                    Collections.singleton(
                    "statEndNow"));
        }
    }
    private void validateSlotRequest(SlotRequestDTO slotRequestDTO, MeetingDTO meeting) {
        long now = Instant.now().toEpochMilli();
        if (slotRequestDTO.getStartDate() == meeting.getStart()
                && slotRequestDTO.getEndDate() == meeting.getEnd()){
            throw new BusinessValidationException(
                    "Slot request time must be diffrent from meeting", Collections.singleton("slotTime"));
        }
        if (slotRequestDTO.getStartDate() < now || slotRequestDTO.getEndDate() < now) {
            throw new BusinessValidationException(
                    "Start/End time cant be smaller than now", Collections.singleton("statEndNow"));
        }
    }

}
