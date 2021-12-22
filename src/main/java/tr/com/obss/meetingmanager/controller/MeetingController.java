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
import tr.com.obss.meetingmanager.dto.IMeetingOrganizerReportDTO;
import tr.com.obss.meetingmanager.dto.IMeetingTimeReportDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.MeetingQueryDTO;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-manager")
@Validated
public class MeetingController {
private final MeetingManagerService meetingManagerService;

    @PostMapping("/meeting")
    @ResponseBody
    public MeetingDTO createMeeting(
           @Valid @RequestBody MeetingDTO meetingDTO) {
         return meetingManagerService.createMeeting(meetingDTO);
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
        return meetingManagerService.updateMeeting(meetingDTO,id);
    }

    @PostMapping("/search-meetings")
    @ResponseBody
    public List<MeetingDTO> createMeeting(
            @Valid @RequestBody MeetingQueryDTO queryDTO) {
        return meetingManagerService.searchMeetings(queryDTO);
    }
    @GetMapping("/meeting-usage")
    @ResponseBody
    public List<IMeetingTimeReportDTO> getMeetingProviderUsageStatistics(
            @RequestParam(value = "start") long  startDate,
            @RequestParam(value = "end") long  endDate) {
        return meetingManagerService.getTimeBasedUsageReport(startDate,endDate);
    }
    @GetMapping("/meetings/{providerId}")
    @ResponseBody
    public List<MeetingDTO> getMeetingByProviderId(@PathVariable String providerId, @RequestParam long start,
                                                       @RequestParam long end){
        return meetingManagerService.getMeetingsByProviderIdBtwRange(providerId,start,end);
    }

    @GetMapping("/meeting-organizers")
    @ResponseBody
    public List<IMeetingOrganizerReportDTO> getMeetingOrganizerStatistics(
            @RequestParam(value = "start") long  startDate,
            @RequestParam(value = "end") long  endDate) {
        return meetingManagerService.getOrganizerReport(startDate,endDate);
    }

    @DeleteMapping("/meeting/{id}")
    public void cancelMeeting(@PathVariable String id){
        meetingManagerService.deleteMeeting(id);
    }

    @GetMapping("/meetings")
    @ResponseBody
    public List<MeetingDTO> listMeetings(@RequestParam(value = "start") long  start,
                                         @RequestParam(value = "end") long  end) {
        return meetingManagerService.listMeetings(start,end);
    }

    @GetMapping("/meeting-slot-requests/{meetingId}")
    @ResponseBody
    public List<SlotRequestDTO> listSlotRequests(@PathVariable String meetingId) {
        return meetingManagerService.getSlotRequests(meetingId);
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
}
