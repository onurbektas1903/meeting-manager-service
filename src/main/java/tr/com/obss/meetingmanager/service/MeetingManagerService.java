package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.QueryObject;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.exception.BusinessValidationException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingMapper;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

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
    public MeetingDTO saveMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = mapper.toEntity(meetingDTO);
        return mapper.toDTO(repository.save(meeting));
    }

    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
        validate(meetingDTO);
        Meeting meeting = repository.findMeetingById(meetingDTO.getId()).orElseThrow(() -> new NotFoundException(
                "Meeting Not Found"));
        mapper.updateMeeting(meetingDTO,meeting);
        return mapper.toDTO(repository.save(meeting)) ;
    }

    public SlotRequestDTO handleChangeMeetingRequest(SlotRequestDTO slotRequestDTO, boolean isApproved) {
//        if (isApproved) {
//            MeetingDTO meetingDTO = meetingManager.findById(slotRequestDTO.getMeetingId());
//            validateSlotRequest(slotRequestDTO,meetingDTO);
//            meetingDTO.setStart(slotRequestDTO.getStartDate());
//            meetingDTO.setEnd(slotRequestDTO.getEndDate());
//
//            slotRequestDTO.setRequestStatus(APPROVED);
//            handlerFactory
//                    .findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType())
//                    .updateMeeting(meetingDTO);
//            return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
//        } else {
//            slotRequestDTO.setRequestStatus(REJECTED);
//            return mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
//        }
        return null;
    }
    public MeetingDTO deleteMeeting(MeetingDTO meetingDTO) {
        return null;
    }
    public List<MeetingDTO> listMeetings(long start,long end) {

        return mapper.toDTOList(repository.findMeetingsBetweenStartAndEndDate(start,end));
    }
    public MeetingDTO findById(String id ){
        return mapper.toDTO(repository.findMeetingById(id).orElseThrow(()-> new NotFoundException(
                "Meeting Not Found")));
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

}
