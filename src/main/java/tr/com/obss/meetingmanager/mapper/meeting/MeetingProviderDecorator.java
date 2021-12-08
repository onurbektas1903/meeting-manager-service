package tr.com.obss.meetingmanager.mapper.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.List;
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
        return delegate.toEntityWithoutAccounts(meetingProviderDTO);
    }

    @Override
    public MeetingProviderDTO toDTOWithoutProviderAccounts(MeetingProvider meetingProvider) {
        return delegate.toDTOWithoutProviderAccounts(meetingProvider);
    }

    @Override
    public MeetingProviderDTO toDTO(MeetingProvider meetingProvider) {
        MeetingProviderDTO meetingProviderDTO = delegate.toDTOWithoutProviderAccounts(meetingProvider);
        List<ProviderAccountDTO> accounts = meetingProvider.getProviderAccounts().parallelStream()
                .map(this::toDTOWithoutProvider).collect(Collectors.toList());
        meetingProviderDTO.setProviderAccounts(accounts);
        return meetingProviderDTO;
    }

    @Override
    public ProviderAccountDTO toDTOWithoutProvider(ProviderAccount pa) {
        return delegate.toDTOWithoutProvider(pa);
    }

    @Override
    public List<MeetingProviderDTO> toDTOList(List<MeetingProvider> meetingProviders) {
       return meetingProviders.parallelStream().map(this::toDTO).collect(Collectors.toList());
    }
}
