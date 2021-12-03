package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.SlotRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Validated
public class MeetingController {
private final MeetHandlerFactory handlerFactory;
private final MeetingManagerService meetingManagerService;
private final SlotRequestService slotRequestService;

    @PostMapping("/create")
    @ResponseBody
    public MeetingDTO createMeeting(
           @Valid @RequestBody MeetingDTO meetingDTO) {
        return handlerFactory.findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType()).createMeeting(meetingDTO);
    }
    @PostMapping("/create-change-meeting-slot-request")
    @ResponseBody
    public SlotRequestDTO createChangeMeetingSlotRequest(
           @Valid @RequestBody SlotRequestDTO slotRequestDTO) {
       return slotRequestService.createChangeSlotRequest(slotRequestDTO);
    }
    @GetMapping("/{id}")
    @ResponseBody
    public MeetingDTO getMeetingById(@PathVariable String id){
        return meetingManagerService.findById(id);
    }

    @PostMapping("/update")
    @ResponseBody
    public MeetingDTO updateMeeting(
            @Valid @RequestBody MeetingDTO meetingDTO) {

        return handlerFactory.findStrategy(meetingDTO.getMeetingProviderType()).updateMeeting(meetingDTO);
    }
    //TODO endpointi düzelt
    @GetMapping("/")
    @ResponseBody
    public List<MeetingDTO> listMeetings(@RequestParam(value = "start") long  start,
                                         @RequestParam(value = "end") long  end) {
        return meetingManagerService.listMeetings(start,end);
//        return meetingManagerService.listMeetings(OffsetDateTime.parse(start).toInstant().toEpochMilli(),
//                OffsetDateTime.parse(end).toInstant().toEpochMilli());
    }

}
