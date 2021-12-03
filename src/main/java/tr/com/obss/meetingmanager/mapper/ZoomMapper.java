package tr.com.obss.meetingmanager.mapper;

import lombok.Data;
import tr.com.obss.meetingmanager.dto.zoom.AccountDTO;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;

@Data
public class ZoomMapper {
    public static ZoomMeetingObjectDTO toZoomMeetObject(MeetingDTO meetingDTO){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(meetingDTO.getProviderAccountDTO().getAdminUserMail());
        accountDTO.setClientId(meetingDTO.getProviderAccountDTO().getClientId());
        accountDTO.setClientSecret(meetingDTO.getProviderAccountDTO().getClientSecret());
        return   ZoomMeetingObjectDTO.builder()
                        .start_time(Long.toString(meetingDTO.getStart()))
                        .topic(meetingDTO.getTitle())
                        .duration(Integer.valueOf(Long.toString((meetingDTO.getEnd()-meetingDTO.getStart())/100000)))
                        .schedule_for(meetingDTO.getOrganizer())
                        .account(accountDTO).build();
    }

}
