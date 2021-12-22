package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import java.util.Map;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoomMeetingService implements MeetingService {
  private final ZoomServiceClient zoomServiceClient;
  private final GoogleMeetingService googleMeetingService;
  private final ZoomMapper zoomMapper;

  @Override
  @Transactional("ptm")
  public MeetingDTO handleCreate(MeetingDTO meetingDTO, Map<String,Object> settings) {
    ZoomMeetingObjectDTO zoomMeeting = zoomMapper.toZoomMeetObject(meetingDTO);
    ZoomMeetingObjectDTO zoomResponse =
        zoomServiceClient.createMeeting(zoomMeeting);
    log.info("Zoom service created meeting",zoomMeeting.getId());
    meetingDTO.setMeetingURL(zoomResponse.getJoin_url());
    meetingDTO.setEventId(zoomResponse.getId());
    googleMeetingService.addMeetingToCalendar(meetingDTO);
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public MeetingDTO handleUpdate(MeetingDTO meetingDTO, Map<String,Object> settings) {
    zoomServiceClient.updateZoomMeeting(meetingDTO.getEventId(), zoomMapper.toZoomMeetObject(meetingDTO));
    googleMeetingService.updateCalendarMeeting(meetingDTO, null,false);
    log.info("Zoom service succesfully updated meeting",meetingDTO.getId());
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public void handleCancel(MeetingDTO meetingDTO) {
      zoomServiceClient.deleteZoomMeeting(meetingDTO.getEventId(),meetingDTO.getProviderAccount());
      googleMeetingService.handleCancel(meetingDTO);
    log.info("Zoom service succesfully cancaled meeting",meetingDTO.getId());

  }

  @Override
  public MeetingProviderTypeEnum getStrategyName() {
    return ZOOM;
  }

}
