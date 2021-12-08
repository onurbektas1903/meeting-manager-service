package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapperDecorator;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetingService;

import javax.transaction.Transactional;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
public class ZoomMeetingService implements MeetingService {
    private final ZoomAccountService zoomAccountService;
    private final ZoomServiceClient zoomServiceClient;
    private final MeetingManagerService meetingManagerService;
    private final GoogleMeetingService googleMeetingService;
    private final ZoomMapperDecorator zoomMapper;
    @Override
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
            ZoomAccountDTO zoomAccount = zoomAccountService.findSuitableAccount(meetingDTO.getStart(), meetingDTO.getEnd(),
                meetingDTO.getMeetingProvider());

//        ZoomMeetingObjectDTO zoomResponse = zoomServiceClient.createMeeting(zoomMapper.toZoomMeetObject(meetingDTO,zoomAccount));
//        meetingDTO.setMeetingURL(zoomResponse.getJoin_url());
//        googleMeetingService.addMeetingToCalendar(meetingDTO);
        meetingDTO.setProviderAccount(ProviderAccountDTO.builder().id(zoomAccount.getId()).build());
       return meetingManagerService.saveMeeting(meetingDTO);
    }


    @Override
    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
        googleMeetingService.updateCalendarMeeting(meetingDTO);
        return meetingManagerService.updateMeeting(meetingDTO);
    }

    @Override
    public MeetingDTO cancelMeeting(MeetingDTO meetingDTO) {
        return null;
    }

    @Override
    public List<MeetingDTO> listMeetings() {
        return null;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return ZOOM;
    }

    @Override
    @Transactional
    public SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved) {
        return null;
    }
}
