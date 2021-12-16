package tr.com.obss.meetingmanager.repository;

import tr.com.obss.meetingmanager.dto.MeetingQueryDTO;
import tr.com.obss.meetingmanager.entity.Meeting;

import java.util.List;

public interface SearchableMeetRepository {
    List<Meeting> searchMeetings(MeetingQueryDTO queryDTO);
}
