package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
import tr.com.obss.meetingmanager.mapper.meeting.MeetingProviderMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderManagerService {

    private final MeetingProviderMapper providerMapper;
    private final MeetingProviderRepository repository;
    private final ProviderAccountManagerService accountManagerService;
    @Transactional("ptm")
    public MeetingProviderDTO saveMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        Set<String> accounts =
                meetingProviderDTO.getProviderAccounts().parallelStream()
                        .map(ProviderAccountDTO::getId).collect(Collectors.toSet());
        List<ProviderAccount> accountList = accountManagerService.findAccountsByIds(accounts);
        MeetingProvider meetingProvider = providerMapper.toEntityWithoutAccounts(meetingProviderDTO);
        accountList.forEach(providerAccount -> providerAccount.setMeetingProvider(meetingProvider));
        meetingProvider.setId(UUID.randomUUID().toString());
        meetingProvider.setProviderAccounts(accountList);
        MeetingProvider saved = repository.save(meetingProvider);
        return providerMapper.toDTO(saved);
    }

    public MeetingProviderDTO findById(String id) {
        MeetingProvider meetingProvider = repository.findById(id).orElseThrow(() -> new NotFoundException(
                "Meeting Provider Not Found"));
        return providerMapper.toDTO(meetingProvider);
    }
    public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    public MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }
    @Transactional("ptm")
    public List<MeetingProviderDTO> listMeetingProviders() {
        return providerMapper.toDTOList( repository.findAll());
        }

}
