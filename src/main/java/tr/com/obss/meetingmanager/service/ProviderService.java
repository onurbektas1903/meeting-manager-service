package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;

public interface ProviderService {
    MeetingProviderDTO createMeetingProvider(MeetingProviderDTO meetingProviderDTO);
    MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO);
    MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO);
    List<MeetingProviderDTO> listMeetingProviders();
    MeetingProviderTypeEnum getStrategyName();

}
