package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.QueryObject;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.mapper.MeetingMapper;
import tr.com.obss.meetingmanager.repository.MeetingRepository;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MeetingManagerService  {
    private final MeetingRepository repository;
    private final MeetingMapper mapper;
    public MeetingDTO saveMeeting(MeetingDTO meetingDTO) {
        Meeting meeting = mapper.toEntity(meetingDTO);
        meeting.setId(UUID.randomUUID().toString());
        return mapper.toDTO(repository.save(meeting));
    }

    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
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
    public List<MeetingDTO> findAll(){
        return null;
    }

    public List<MeetingDTO> getOccupiedSlots(QueryObject queryObject, MeetingProviderDTO meetingProvider){
        return null;
    }

    private void validate(MeetingDTO meetingDTO){


    }

}
