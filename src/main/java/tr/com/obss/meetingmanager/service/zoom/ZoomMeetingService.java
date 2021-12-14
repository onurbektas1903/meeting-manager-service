package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapperDecorator;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoomMeetingService implements MeetingService {
  private final ZoomAccountService zoomAccountService;
  private final ZoomServiceClient zoomServiceClient;
  private final GoogleMeetingService googleMeetingService;
  private final ZoomMapperDecorator zoomMapper;

  @Override
  @Transactional("ptm")
  public MeetingDTO handleCreate(MeetingDTO meetingDTO) {
    ZoomAccountDTO zoomAccount =
        zoomAccountService.findSuitableAccount(
            meetingDTO.getStart(), meetingDTO.getEnd(), meetingDTO.getMeetingProvider());

    ZoomMeetingObjectDTO zoomResponse =
        zoomServiceClient.createMeeting(zoomMapper.toZoomMeetObject(meetingDTO, zoomAccount));
    log.info("Zoom service created meeting");
    meetingDTO.setMeetingURL(zoomResponse.getJoin_url());
    meetingDTO.setEventId(zoomResponse.getId());

    googleMeetingService.addMeetingToCalendar(meetingDTO);
    meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(zoomAccount.getId()).build());
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public MeetingDTO handleUpdate(MeetingDTO meetingDTO) {
    ZoomAccountDTO zoomAccount = zoomAccountService.findById(meetingDTO.getProviderAccount().getId());
    zoomServiceClient.updateZoomMeeting(meetingDTO.getEventId(), zoomMapper.toZoomMeetObject(meetingDTO, zoomAccount));
    googleMeetingService.updateCalendarMeeting(meetingDTO, false);
    return meetingDTO;
  }

  @Override
  @Transactional("ptm")
  public void handleCancel(MeetingDTO meetingDTO) {
      ZoomAccountDTO zoomAccount = zoomAccountService.findById(meetingDTO.getProviderAccount().getId());
      zoomServiceClient.deleteZoomMeeting(meetingDTO.getEventId(),zoomAccount);
      googleMeetingService.handleCancel(meetingDTO);
  }

  @Override
  public MeetingProviderTypeEnum getStrategyName() {
    return ZOOM;
  }
}
