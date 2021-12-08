package tr.com.obss.meetingmanager.mapper.google;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(GoogleMapperDecorator.class)
public interface GoogleMapper {
    @Mapping(target = "accountDetails",ignore = true)
    @Mapping(target = "meetingProvider.providerAccounts",ignore = true)
    GoogleAccountDTO toGoogleAccountDTO(ProviderAccount providerAccount);

    @Mapping(target = "accountDetails",ignore = true)
    ProviderAccount toEntity(GoogleAccountDTO googleAccountDTO);

    List<GoogleAccountDTO> toDTOList(List<ProviderAccount> accounts);


}
