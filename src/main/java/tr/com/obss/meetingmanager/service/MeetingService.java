package tr.com.obss.meetingmanager.service;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;

public interface MeetingService {
    MeetingDTO createMeeting(MeetingDTO meetingDTO);
    MeetingDTO updateMeeting(MeetingDTO meetingDTO);
    MeetingDTO deleteMeeting(MeetingDTO meetingDTO);
    List<MeetingDTO> listMeetings();
    MeetingProviderTypeEnum getStrategyName();
}
