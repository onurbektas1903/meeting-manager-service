package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

public interface ProviderService {
    MeetingProviderDTO validateConferenceSettings(MeetingProviderDTO meetingProviderDTO);
    MeetingProviderTypeEnum getStrategyName();
}
