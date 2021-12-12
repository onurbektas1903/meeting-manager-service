package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.service.SlotRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-manager")
@Validated
public class MeetingController {
private final MeetingManagerService meetingManagerService;
private final SlotRequestService slotRequestService;

    @PostMapping("/meeting")
    @ResponseBody
    public MeetingDTO createMeeting(
           @Valid @RequestBody MeetingDTO meetingDTO) {
        return meetingManagerService.createMeeting(meetingDTO);
    }
    @PostMapping("/meeting-slot-request")
    @ResponseBody
    public SlotRequestDTO createChangeMeetingSlotRequest(
           @Valid @RequestBody SlotRequestDTO slotRequestDTO) {
       return meetingManagerService.addSlotRequestToMeeting(slotRequestDTO);
    }

    @PutMapping("/meeting-slot-request/{isApproved}")
    @ResponseBody
    public SlotRequestDTO handleSlotRequestApproval(@RequestBody SlotRequestDTO slotRequest,@PathVariable boolean isApproved) {
        return meetingManagerService.handleRequestApproval(slotRequest,isApproved);
    }
    @DeleteMapping("/meeting-slot-request/{id}")
    public SlotRequestDTO deleteSlotRequetsById(@PathVariable String id){
        return meetingManagerService.removeSlotRequest(id);
    }
    @DeleteMapping("/meeting/{id}")
    public void cancelMeeting(@PathVariable String id){
         meetingManagerService.deleteMeeting(id);
    }

    @GetMapping("/meeting-slot-requests/{meetingId}")
    @ResponseBody
    public List<SlotRequestDTO> getSlotRequestsByMeeting(
           @Valid @PathVariable String meetingId) {
       return slotRequestService.getSlotRequestsByMeetingId(meetingId);
    }

    @GetMapping("/meeting/{id}")
    @ResponseBody
    public MeetingDTO getMeetingById(@PathVariable String id){
        return meetingManagerService.findMeetingById(id);
    }

    @PutMapping("/meeting/{id}")
    @ResponseBody
    public MeetingDTO updateMeeting(@RequestBody MeetingDTO meetingDTO,
                                                @PathVariable String id) {
        //TODO delete id
        return meetingManagerService.updateMeeting(meetingDTO);
    }
    //TODO endpointi düzelt
    @GetMapping("/meetings")
    @ResponseBody
    public List<MeetingDTO> listMeetings(@RequestParam(value = "start") long  start,
                                         @RequestParam(value = "end") long  end) {
        return meetingManagerService.listMeetings(start,end);
    }

}
