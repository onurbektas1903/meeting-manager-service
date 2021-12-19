package tr.com.obss.meetingmanager.mapper.meeting;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(MeetingProviderDecorator.class)
public interface MeetingProviderMapper {

    MeetingProvider toEntityWithoutAccounts(MeetingProviderDTO meetingProviderDTO);

    MeetingProvider toEntity(MeetingProviderDTO meetingProviderDTO);

    MeetingProviderDTO toDTOWithoutProviderAccounts(MeetingProvider meetingProvider);

    @Named(value = "toDTO")
    MeetingProviderDTO toDTO(MeetingProvider meetingProvider);

    List<MeetingProviderDTO> toDTOList(List<MeetingProvider> meetingProviders);

    @Mapping(target = "meetingProviderType",ignore = true)
    void updateProvider(MeetingProviderDTO dto, @MappingTarget MeetingProvider entity);

}
