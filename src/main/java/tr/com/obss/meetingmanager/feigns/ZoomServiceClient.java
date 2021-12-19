package tr.com.obss.meetingmanager.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;

import static org.springframework.http.HttpStatus.OK;

@FeignClient(value = "zoom-service")
public interface ZoomServiceClient {

  @PostMapping("/zoom-event-manager/meeting")
  ZoomMeetingObjectDTO createMeeting(@RequestBody ZoomMeetingObjectDTO zmo);

  @PutMapping("/zoom-event-manager/meeting/{meetingId}")
  @ResponseBody
  ZoomMeetingObjectDTO updateZoomMeeting(
      @PathVariable String meetingId, @RequestBody ZoomMeetingObjectDTO zmo);

  @DeleteMapping("/zoom-event-manager/meeting/{meetingId}")
   void deleteZoomMeeting(@PathVariable String meetingId, @RequestParam String accountId);

  @PostMapping("/zoom-account-manager/account")
   ZoomAccountDTO createZoomAccount(@RequestBody ZoomAccountDTO zmo);

  @PutMapping("/zoom-account-manager/account/{accountMail}")
  ZoomAccountDTO updateAccount(@PathVariable String accountMail,  ZoomAccountDTO accountDTO);

  @DeleteMapping("/zoom-account-manager/account/{accountMail}")
  @ResponseBody
  @ResponseStatus(OK)
   void deleteAccount(@PathVariable  String accountMail);

}
