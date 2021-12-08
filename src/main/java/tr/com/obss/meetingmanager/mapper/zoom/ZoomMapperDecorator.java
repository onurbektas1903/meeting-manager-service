package tr.com.obss.meetingmanager.mapper.zoom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDetails;
import tr.com.obss.meetingmanager.dto.zoom.ZoomMeetingObjectDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Primary
@Slf4j
public class ZoomMapperDecorator implements ZoomMapper {
    TypeReference<HashMap<String,String>> typeRef
            = new TypeReference<HashMap<String,String>>() {};
    private ZoomMapper delegate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    @Qualifier("delegate")
    public void setDelegate(ZoomMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ZoomAccountDTO toDTO(ProviderAccount providerAccount) {
        ZoomAccountDTO dto = delegate.toDTO(providerAccount);
        dto.setAccountDetails(objectMapper.convertValue(providerAccount.getAccountDetails(), ZoomAccountDetails.class));
        return dto;
    }

    @Override
    public ProviderAccount toEntity(ZoomAccountDTO zoomAccountDTO) {
        ProviderAccount providerAccount = delegate.toEntity(zoomAccountDTO);
        providerAccount.setMeetingProviderType(ZOOM);
        if(providerAccount.getId() == null || providerAccount.getId().isEmpty() ){
            providerAccount.setId(UUID.randomUUID().toString());
        }
        providerAccount.setAccountDetails(objectMapper.convertValue(zoomAccountDTO.getAccountDetails(),typeRef));
        return providerAccount;
    }

    @Override
    public List<ZoomAccountDTO> toDTOList(List<ProviderAccount> accounts) {
        return accounts.parallelStream().map(this::toDTO).collect(Collectors.toList());
    }

    public  ZoomMeetingObjectDTO toZoomMeetObject(MeetingDTO meetingDTO,ZoomAccountDTO zoomAccount){
        return   ZoomMeetingObjectDTO.builder()
                .start_time(Long.toString(meetingDTO.getStart()))
                .topic(meetingDTO.getTitle())
                .duration(Integer.valueOf(Long.toString((meetingDTO.getEnd()-meetingDTO.getStart())/100000)))
                .schedule_for(meetingDTO.getOrganizer())
                .account(zoomAccount).build();
    }
}
