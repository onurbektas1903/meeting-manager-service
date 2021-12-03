package tr.com.obss.meetingmanager.service.zoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomSettingsDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.mapper.MeetingProviderMapper;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.ProviderService;

import javax.transaction.Transactional;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@Data
@RequiredArgsConstructor
public class ZoomProviderService implements ProviderService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProviderManagerService providerManagerService;
    @Override
    @Transactional
    public MeetingProviderDTO createMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        //TODO validate
        ZoomSettingsDTO zoomSettings = mapper.convertValue(meetingProviderDTO.getSettings(), ZoomSettingsDTO.class);
        return providerManagerService.saveMeetingProvider(meetingProviderDTO);
    }

    @Override
    public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    @Override
    public MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    @Override
    public List<MeetingProviderDTO> listMeetingProviders() {

        return null;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return ZOOM;
    }
}
