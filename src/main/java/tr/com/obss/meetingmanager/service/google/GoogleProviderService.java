package tr.com.obss.meetingmanager.service.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.common.exceptions.BusinessValidationException;
import tr.com.obss.meetingmanager.dto.google.GoogleSettingsDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomSettingsDTO;
import tr.com.obss.meetingmanager.service.ProviderService;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.service.ProviderManagerService;

import java.util.Collections;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class GoogleProviderService implements ProviderService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @Transactional("ptm")

    public MeetingProviderDTO validateConferenceSettings(MeetingProviderDTO meetingProviderDTO) {
        GoogleSettingsDTO googleSettings = mapper.convertValue(meetingProviderDTO.getSettings(), GoogleSettingsDTO.class);

        if(googleSettings.getPassword() != null && googleSettings.getPassword().length() < 3 && googleSettings.getPassword().length() >128){
            throw new BusinessValidationException("Password lenghth should between 3 and 128", Collections.singletonMap(
                    "passwordLength","1-128"));
        }
        log.info("Google settings success fully converted");
        return meetingProviderDTO;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return MeetingProviderTypeEnum.GOOGLE;
    }
}
