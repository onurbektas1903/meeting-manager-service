package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.service.MeetingManagerService;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.service.ProviderManagerService;

import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE_MEET;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;
import static tr.com.obss.meetingmanager.mapper.CalendarMeetMapper.toCalendarEventDTO;

@Service
@RequiredArgsConstructor
public class ZoomMeetingService implements MeetingService {
    private final GoogleCalendarServiceClient calendarClientService;
    private final ZoomServiceClient zoomServiceClient;
    private final MeetingManagerService meetingManagerService;
    private final ProviderManagerService providerManagerService;
    @Override
    public MeetingDTO createMeeting(MeetingDTO meetingDTO) {
        MeetingProviderDTO meetingProvider = providerManagerService.findByMeetingProviderType(GOOGLE_MEET);
        ProviderAccountDTO calendarAccount = meetingProvider.getProviderAccounts().get(0);
        ProviderAccountDTO meetingAccount = providerManagerService.getSuitableAccount(meetingDTO.getStart(), meetingDTO.getEnd(),
                meetingDTO.getMeetingProvider());
        meetingDTO.setId(UUID.randomUUID().toString());
        meetingDTO.setProviderAccountDTO(meetingAccount);
//        ZoomMeetingObjectDTO zoomResponse = zoomServiceClient.createMeeting(toZoomMeetObject(meetingDTO));
//        meetingDTO.setMeetingURL(zoomResponse.getJoin_url());
        CalendarEventDTO calendarEventDTO = toCalendarEventDTO(meetingDTO);
        calendarEventDTO.setCreateMeeting(false);
        calendarEventDTO.setDescription(meetingDTO.getMeetingURL());
        calendarEventDTO.setAccountEmail(calendarAccount.getAccountMail());
        calendarEventDTO.setAdminUserEmail(calendarAccount.getAdminUserMail());
        calendarEventDTO.setCredentialsFileName(calendarAccount.getFileName());
//        CalendarEventDTO eventDTO = calendarClientService.scheduleMeeting(calendarEventDTO);
        meetingManagerService.saveMeeting(meetingDTO);
        return meetingDTO;
    }


    @Override
    public MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
        return null;
    }

    @Override
    public MeetingDTO deleteMeeting(MeetingDTO meetingDTO) {
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
}
