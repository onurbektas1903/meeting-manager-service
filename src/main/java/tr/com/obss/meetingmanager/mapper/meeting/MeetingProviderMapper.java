package tr.com.obss.meetingmanager.mapper.meeting;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapperDecorator;

import java.util.List;
import java.util.UUID;
@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(MeetingProviderDecorator.class)
public interface MeetingProviderMapper {
    @Mapping(target = "providerAccounts",ignore = true)
    MeetingProvider toEntityWithoutAccounts(MeetingProviderDTO meetingProviderDTO);

    @Mapping(target = "providerAccounts",ignore = true)
    MeetingProviderDTO toDTOWithoutProviderAccounts(MeetingProvider meetingProvider);

    @Named(value = "toDTO")
    MeetingProviderDTO toDTO(MeetingProvider meetingProvider);

    @Mapping(target = "meetingProvider",ignore = true)
    ProviderAccountDTO toDTOWithoutProvider(ProviderAccount pa);

    List<MeetingProviderDTO> toDTOList(List<MeetingProvider> meetingProviders);

}
