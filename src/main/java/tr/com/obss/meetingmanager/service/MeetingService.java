package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;


public interface MeetingService {
    MeetingDTO handleCreate(MeetingDTO meetingDTO);
    MeetingDTO handleUpdate(MeetingDTO meetingDTO);
    void handleCancel(MeetingDTO meetingDTO);
    MeetingProviderTypeEnum getStrategyName();
    void handleRollback(MeetingDTO meetingDTO);

}
