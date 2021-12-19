package tr.com.obss.meetingmanager.mapper.zoom;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;
import tr.com.obss.meetingmanager.util.TimeUtil;

import java.util.HashMap;

import static tr.com.obss.meetingmanager.util.TimeUtil.convertAndGetDate;

@Configuration
public class ZoomMapper {
    private final int zoomScheduledMeetingType = 2;
    TypeReference<HashMap<String,String>> typeRef
            = new TypeReference<>() {
    };
    //TODO handle settings
    public ZoomMeetingObjectDTO toZoomMeetObject(MeetingDTO meetingDTO){

        return   ZoomMeetingObjectDTO.builder()
                .start_time( convertAndGetDate(meetingDTO.getStart(),"Turkey"))
                .topic(meetingDTO.getTitle())
                .type(zoomScheduledMeetingType)
                .duration(TimeUtil.findDiffrenceAsMinutes( meetingDTO.getStart(),meetingDTO.getEnd()))
                .accountId(meetingDTO.getProviderAccount()).build();
    }
}
