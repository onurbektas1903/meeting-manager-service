package tr.com.obss.meetingmanager.service.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE_MEET;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    public MeetingDTO scheduleMeeting(MeetingDTO meetingDTO) {
        return null;
    }
    public MeetingDTO createAccount(MeetingDTO meetingDTO) {
        return null;
    }

}