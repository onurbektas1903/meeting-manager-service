package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.mapper.SlotRequestMapper;
import tr.com.obss.meetingmanager.repository.SlotRequestRepository;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotRequestService {

    private final GoogleMeetingService googleMeetingService;
    private final SlotRequestRepository repository;
    private final SlotRequestMapper mapper;

    @Transactional
    public SlotRequestDTO createChangeSlotRequest(SlotRequestDTO slotRequestDTO){
        googleMeetingService.sendChangeSlotMail(slotRequestDTO);
     return  mapper.toDTO(repository.save(mapper.toEntity(slotRequestDTO)));
    }

}
