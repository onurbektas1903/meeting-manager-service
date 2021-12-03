package tr.com.obss.meetingmanager.mapper;

import org.mapstruct.Named;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.List;
import java.util.UUID;

@org.mapstruct.Mapper(componentModel = "spring")
public interface MeetingProviderMapper {

    MeetingProvider toEntityWithAccounts(MeetingProviderDTO meetingProviderDTO);
    @Named(value = "toEntity")
    default MeetingProvider toEntity(MeetingProviderDTO meetingProviderDTO){
        MeetingProvider meetingProvider = toEntityWithAccounts(meetingProviderDTO);
    meetingProvider
        .getProviderAccounts()
        .forEach(
            providerAccount -> {
              providerAccount.setId(UUID.randomUUID().toString());
              providerAccount.setMeetingProvider(meetingProvider);
            });

        return meetingProvider;
    }

    MeetingProviderDTO toDTO(MeetingProvider meetingProviderDTO);

    ProviderAccountDTO toDTO(ProviderAccount pa);
    ProviderAccount toEntity(ProviderAccountDTO pat);

    List<MeetingProviderDTO> toDTOList(List<MeetingProvider> meetingProviders);

}
