package tr.com.obss.meetingmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.entity.Recipient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MeetingMapper {
    @Mapping(ignore = true, target = "recipients")
    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    MeetingDTO toDTOObj(Meeting meeting);

    RecipientDTO toDTO(Recipient recipient);
    MeetingProviderDTO toDTO(MeetingProvider meetingProviderDTO);

   default MeetingDTO toDTO(Meeting meeting){
        MeetingDTO meetingDTO = toDTOObj(meeting);
        meetingDTO.setProviderAccountDTO(toDTO(meeting.getProviderAccount()));
        meetingDTO.setMeetingProvider(toDTO(meeting.getProviderAccount().getMeetingProvider()));
        meetingDTO.setRecipients(new ArrayList<>());
        meeting.getRecipients().forEach(recipient ->
                meetingDTO.getRecipients().add(toDTO(recipient)));
        return meetingDTO;
    }
     Recipient toEntity(RecipientDTO recipientDTO);
    default List<MeetingDTO> toDTOList(List<Meeting> meetings){
        ArrayList<MeetingDTO> resultList = new ArrayList<>();
        if(meetings.isEmpty()){
            return resultList;
        }
        meetings.forEach(meeting -> resultList.add(toDTOObj(meeting)));
        return resultList;
    }
    ProviderAccountDTO toDTO(ProviderAccount pa);
    ProviderAccount toEntity(ProviderAccountDTO pat);

    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    @Mapping(ignore = true, target = "recipients")
    Meeting toEntityObj(MeetingDTO meeting);

   default Meeting toEntity(MeetingDTO meetingDTO){
       Meeting meeting = toEntityObj(meetingDTO);
       meeting.setRecipients(new ArrayList<>());
       meetingDTO.getRecipients().forEach(recipient ->{
           recipient.setId(UUID.randomUUID().toString());
           Recipient recipientEntity = toEntity(recipient);
           recipientEntity.setMeeting(meeting);
           meeting.getRecipients()
                   .add(recipientEntity);
       } );
       meeting.setProviderAccount(toEntity(meetingDTO.getProviderAccountDTO()));
       return meeting;
   }
}
