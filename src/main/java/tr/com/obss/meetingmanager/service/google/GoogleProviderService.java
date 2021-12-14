package tr.com.obss.meetingmanager.service.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.service.ProviderService;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.service.ProviderManagerService;

@Service
@Data
@RequiredArgsConstructor
public class GoogleProviderService implements ProviderService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProviderManagerService providerManagerService;

    @Override
    @Transactional("ptm")
    public MeetingProviderDTO createMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return providerManagerService.saveMeetingProvider(meetingProviderDTO);
    }

    @Override
    public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO,String id) {
        return null;
    }

    @Override
    public MeetingProviderTypeEnum getStrategyName() {
        return MeetingProviderTypeEnum.GOOGLE;
    }
}
