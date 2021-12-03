package tr.com.obss.meetingmanager.service.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomSettingsDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.ProviderService;

import javax.transaction.Transactional;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE_MEET;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@Data
@RequiredArgsConstructor
public class GoogleMeetProviderService  {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProviderManagerService providerManagerService;
    private final GoogleCalendarServiceClient calendarService;
    
    @Transactional
    public MeetingProviderDTO createMeetingProvider(MeetingProviderDTO meetingProviderDTO, MultipartFile multipartFile) {
        calendarService.createAccount(multipartFile);
        return providerManagerService.saveMeetingProvider(meetingProviderDTO);
    }

    
    public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    
    public MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    
    public List<MeetingProviderDTO> listMeetingProviders() {

        return null;
    }

    
    public MeetingProviderTypeEnum getStrategyName() {
        return GOOGLE_MEET;
    }
}
