package tr.com.obss.meetingmanager.service.zoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.common.exceptions.BusinessValidationException;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomSettingsDTO;
import tr.com.obss.meetingmanager.service.ProviderService;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.service.ProviderManagerService;


import java.util.Collections;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@Data
@RequiredArgsConstructor
public class ZoomProviderService implements ProviderService {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    @Transactional("ptm")
    public MeetingProviderDTO validateConferenceSettings(MeetingProviderDTO meetingProviderDTO) {
        ZoomSettingsDTO zoomSettings = mapper.convertValue(meetingProviderDTO.getSettings(), ZoomSettingsDTO.class);
        if(zoomSettings.getWaiting_room() && zoomSettings.getJoin_before_host()){
            throw new BusinessValidationException("If waiting room property is enabled join before host must be " +
                    "disabled", Collections.singleton("joinBeforeHost"));
        }
        if(zoomSettings.getPassword() != null && zoomSettings.getPassword().length() < 3 && zoomSettings.getPassword().length() >128){
            throw new BusinessValidationException("Password lenghth should between 3 and 128", Collections.singletonMap(
                    "passwordLength","1-128"));
        }
        return meetingProviderDTO;
    }

    @Override
    @Transactional("ptm")
    public MeetingProviderTypeEnum getStrategyName() {
        return ZOOM;
    }
}
