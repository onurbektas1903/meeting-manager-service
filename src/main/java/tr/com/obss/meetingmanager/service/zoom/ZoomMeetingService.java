package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.common.exceptions.ServiceNotAvailableException;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.sender.KafkaMessageSender;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoomMeetingService implements MeetingService {
  private final ZoomServiceClient zoomServiceClient;
  private final GoogleMeetingService googleMeetingService;
  private final ZoomMapper zoomMapper;
  private final KafkaMessageSender messageSender;
  @Value("${topics.zoom-error}")
  private String zoomErrorTopic;
  @Override
  @Transactional("ptm")
  public MeetingDTO handleCreate(MeetingDTO meetingDTO) {

    ZoomMeetingObjectDTO zoomResponse =
        zoomServiceClient.createMeeting(zoomMapper.toZoomMeetObject(meetingDTO));
    log.info("Zoom service created meeting");
    meetingDTO.setMeetingURL(zoomResponse.getJoin_url());
    meetingDTO.setEventId(zoomResponse.getId());
    try{
      googleMeetingService.addMeetingToCalendar(meetingDTO);
    }catch (ServiceNotAvailableException e){
      messageSender.sendDeleted(zoomErrorTopic,zoomMapper.toZoomMeetObject(meetingDTO));
      log.error(e.getMessage(),e);
      throw e;
    }
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public MeetingDTO handleUpdate(MeetingDTO meetingDTO) {
    zoomServiceClient.updateZoomMeeting(meetingDTO.getEventId(), zoomMapper.toZoomMeetObject(meetingDTO));
    googleMeetingService.updateCalendarMeeting(meetingDTO, false);
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public void handleCancel(MeetingDTO meetingDTO) {
      zoomServiceClient.deleteZoomMeeting(meetingDTO.getEventId(),meetingDTO.getProviderAccount());
      googleMeetingService.handleCancel(meetingDTO);
  }

  @Override
  public MeetingProviderTypeEnum getStrategyName() {
    return ZOOM;
  }
}
