package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;

public interface MeetingService {
    MeetingDTO handleCreate(MeetingDTO meetingDTO);
    MeetingDTO handleUpdate(MeetingDTO meetingDTO);
    MeetingDTO handleCancel(MeetingDTO meetingDTO);
    List<MeetingDTO> listMeetings();
    MeetingProviderTypeEnum getStrategyName();
}
