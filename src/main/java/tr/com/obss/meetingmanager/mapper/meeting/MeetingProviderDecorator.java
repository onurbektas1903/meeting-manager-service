package tr.com.obss.meetingmanager.mapper.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Slf4j
public class MeetingProviderDecorator implements MeetingProviderMapper{
    private MeetingProviderMapper delegate;
    @Autowired
    @Qualifier("delegate")
    public void setDelegate(MeetingProviderMapper delegate) {
        this.delegate = delegate;
    }
    @Override
    public MeetingProvider toEntityWithoutAccounts(MeetingProviderDTO meetingProviderDTO) {
        MeetingProvider meetingProvider = delegate.toEntityWithoutAccounts(meetingProviderDTO);
        if(meetingProvider.getId()==null){
            meetingProvider.setId(UUID.randomUUID().toString());
        }
        return meetingProvider;
    }

    @Override
    public MeetingProvider toEntity(MeetingProviderDTO meetingProviderDTO) {
        MeetingProvider provider = delegate.toEntity(meetingProviderDTO);
        if (provider.getId() == null){
            provider.setId(UUID.randomUUID().toString());
        }
        return provider;
    }

    @Override
    public MeetingProviderDTO toDTOWithoutProviderAccounts(MeetingProvider meetingProvider) {
        return delegate.toDTOWithoutProviderAccounts(meetingProvider);
    }

    @Override
    public MeetingProviderDTO toDTO(MeetingProvider meetingProvider) {
        return delegate.toDTO(meetingProvider);
    }


    @Override
    public List<MeetingProviderDTO> toDTOList(List<MeetingProvider> meetingProviders) {
       return meetingProviders.parallelStream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void updateProvider(MeetingProviderDTO dto, MeetingProvider entity) {
        delegate.updateProvider(dto,entity);
    }
}
