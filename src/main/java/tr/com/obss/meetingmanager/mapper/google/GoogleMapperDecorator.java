package tr.com.obss.meetingmanager.mapper.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDetails;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleMailDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Slf4j
public class GoogleMapperDecorator implements GoogleMapper {
    private GoogleMapper delegate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    @Qualifier("delegate")
    public void setDelegate(GoogleMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public GoogleAccountDTO toGoogleAccountDTO(ProviderAccount providerAccount) {
        GoogleAccountDTO dto = delegate.toGoogleAccountDTO(providerAccount);
        dto.setAccountDetails(objectMapper.convertValue(providerAccount.getAccountDetails(), GoogleAccountDetails.class));
        return dto;
    }

    @Override
    public ProviderAccount toEntity(GoogleAccountDTO googleAccountDTO) {
        ProviderAccount providerAccount = delegate.toEntity(googleAccountDTO);
        providerAccount.setMeetingProviderType(MeetingProviderTypeEnum.GOOGLE);
        providerAccount.setAccountDetails(objectMapper.convertValue(googleAccountDTO.getAccountDetails(),Map.class));
        if(providerAccount.getId() == null || providerAccount.getId().isEmpty() ){
            providerAccount.setId(UUID.randomUUID().toString());
        }
        return providerAccount;
    }

    @Override
    public List<GoogleAccountDTO> toDTOList(List<ProviderAccount> accounts) {
        return accounts.parallelStream().map(this::toGoogleAccountDTO).collect(Collectors.toList());
    }

    public  GoogleMailDTO toGoogleMailDTO(SlotRequestDTO slotRequestDTO, GoogleAccountDTO account){
        return
                GoogleMailDTO.builder()
                        .subject(slotRequestDTO.getTitle())
                        .body(slotRequestDTO.getDescription())
                        .senderMail(slotRequestDTO.getCreator())
                        .recipientMail(slotRequestDTO.getOrganizer())
                        .meetingLink(slotRequestDTO.getMeetingLink())
                        .account(account)
                        .build();
    }
    public DeleteEventDTO toDeleteEventDTO(GoogleAccountDTO googleAccountDTO, MeetingDTO meetingDTO){
       return DeleteEventDTO.builder()
                 .account(googleAccountDTO)
                 .creator(meetingDTO.getOrganizer())
                 .build();
    }

    public CalendarEventDTO toCalendarEventDTO(MeetingDTO meetingDTO,GoogleAccountDTO googleAccount,boolean withMeet){

        //TODO googleEventSettings i convertle ve setle
//        ProviderAccountDTO providerAccount = meetingDTO.getProviderAccountDTO();
        meetingDTO.setCalendarEventId(UUID.randomUUID().toString());
        return CalendarEventDTO.builder()
                .meetingUrl(meetingDTO.getMeetingURL())
                .start(meetingDTO.getStart())
                .creator(meetingDTO.getOrganizer())
                .end(meetingDTO.getEnd())
                .summary(meetingDTO.getTitle())
                .description(meetingDTO.getDescription())
                .eventId(meetingDTO.getCalendarEventId())
                .account(googleAccount)
                .createMeeting(withMeet)
                .eventAttendees(meetingDTO.getRecipients()
                        .stream().map(RecipientDTO::getName).collect(Collectors.toSet())).build();
    }
}
