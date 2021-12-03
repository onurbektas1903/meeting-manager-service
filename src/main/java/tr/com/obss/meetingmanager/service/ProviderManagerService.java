package tr.com.obss.meetingmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.exception.MeetingOccupiedException;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.mapper.MeetingProviderMapper;
import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;

@Service
@RequiredArgsConstructor
public class ProviderManagerService {

    private final MeetingProviderMapper providerMapper;
    private final MeetingProviderRepository repository;
    @Transactional
    public MeetingProviderDTO saveMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        MeetingProvider meetingProvider = providerMapper.toEntity(meetingProviderDTO);
        meetingProvider.setId(UUID.randomUUID().toString());
        MeetingProvider saved = repository.save(meetingProvider);
        return providerMapper.toDTO(saved);
    }
    public MeetingProviderDTO findByMeetingProviderType(MeetingProviderTypeEnum type){
        return providerMapper.toDTO(repository.findMeetingProviderByMeetingProviderType(type).orElseThrow(()-> new NotFoundException(
                "Calendar Provider Not Found")));
    }

    public ProviderAccountDTO getSuitableAccount(long startDate, long endDate, MeetingProviderDTO providerDTO){
        if(providerDTO.getConferenceType() == POOL){
            return findFreeAccountsForGivenDateRange(startDate, endDate, providerDTO);
        }else{
            MeetingProvider provider = findById(providerDTO.getId());
            if(provider.getProviderAccounts().isEmpty()){
                throw new NotFoundException("Provider Account Not Found");
            }
            return providerMapper.toDTO(provider.getProviderAccounts().get(0));
        }
    }

    public MeetingProvider findById(String id) {
        return  repository.findById(id).orElseThrow(() -> new NotFoundException(
                "Calendar Provider Not Found"));
    }

    private ProviderAccountDTO findFreeAccountsForGivenDateRange(long startDate, long endDate, MeetingProviderDTO providerDTO) {
         List<ProviderAccountDTO> freeAccounts =repository.findFreeAccounts(startDate, endDate, providerDTO.getId());
                 if(freeAccounts.isEmpty()){
                    throw  new MeetingOccupiedException("Meeting occupied for selected provider",
                             Collections.singleton(providerDTO.getName()));
                 }

         return freeAccounts.get(0);
    }


    public MeetingProviderDTO updateMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    public MeetingProviderDTO deleteMeetingProvider(MeetingProviderDTO meetingProviderDTO) {
        return null;
    }

    public List<MeetingProviderDTO> listMeetingProviders() {
        return providerMapper.toDTOList( repository.findAll());
        }

}
