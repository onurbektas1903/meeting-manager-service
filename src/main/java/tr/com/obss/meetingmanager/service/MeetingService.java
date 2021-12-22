package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.Map;


public interface MeetingService {
    MeetingDTO handleCreate(MeetingDTO meetingDTO, Map<String,Object> settings);
    MeetingDTO handleUpdate(MeetingDTO meetingDTO, Map<String,Object> settings);
    void handleCancel(MeetingDTO meetingDTO);
    MeetingProviderTypeEnum getStrategyName();

}
