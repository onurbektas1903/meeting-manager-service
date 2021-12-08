package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;

public interface MeetingService {
    MeetingDTO createMeeting(MeetingDTO meetingDTO);
    MeetingDTO updateMeeting(MeetingDTO meetingDTO);
    MeetingDTO cancelMeeting(MeetingDTO meetingDTO);
    List<MeetingDTO> listMeetings();
    MeetingProviderTypeEnum getStrategyName();
    SlotRequestDTO handleRequestApproval(SlotRequestDTO slotRequestDTO, boolean isApproved);
}
