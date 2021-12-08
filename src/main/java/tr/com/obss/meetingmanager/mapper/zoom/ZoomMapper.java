package tr.com.obss.meetingmanager.mapper.zoom;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(ZoomMapperDecorator.class)
public interface ZoomMapper {
    @Mapping(target = "accountDetails", ignore = true)
    @Mapping(target = "meetingProvider.providerAccounts",ignore = true)
    ZoomAccountDTO toDTO(ProviderAccount providerAccount);

    @Mapping(target = "accountDetails",ignore = true)
    ProviderAccount toEntity(ZoomAccountDTO zoomAccountDTO);

    List<ZoomAccountDTO> toDTOList(List<ProviderAccount> accounts);

}
