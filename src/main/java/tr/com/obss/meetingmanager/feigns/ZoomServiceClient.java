package tr.com.obss.meetingmanager.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;

@FeignClient(value = "zoom-service")
public interface ZoomServiceClient {

  @PostMapping("zoom-manager/meeting")
  ZoomMeetingObjectDTO createMeeting(@RequestBody ZoomMeetingObjectDTO zmo);

  @PutMapping("zoom-manager/meeting/{meetingId}")
  @ResponseBody
  ZoomMeetingObjectDTO updateZoomMeeting(
      @PathVariable String meetingId, @RequestBody ZoomMeetingObjectDTO zmo);

  @DeleteMapping("zoom-manager/meeting/{meetingId}")
  void deleteZoomMeeting(@PathVariable String meetingId, @RequestBody ZoomAccountDTO account);
}
