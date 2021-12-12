package tr.com.obss.meetingmanager.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;

@FeignClient(value = "zoom-service")
public interface ZoomServiceClient {

    @PostMapping("zoom-manager/meeting")
    ZoomMeetingObjectDTO createMeeting(@RequestBody ZoomMeetingObjectDTO zmo);
}
