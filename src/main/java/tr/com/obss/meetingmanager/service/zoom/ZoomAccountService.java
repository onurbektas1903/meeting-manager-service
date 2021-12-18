package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDetails;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDetails;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.exception.NotUniqueException;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "zoomAccountCache")
public class ZoomAccountService {
    private final ProviderAccountRepository repository;
    private final ZoomMapper mapper;
    private final ProviderAccountManagerService accountManager;
    
    @Transactional("ptm")
    @CacheEvict(cacheNames = "zoomAccounts", allEntries = true)
    public ZoomAccountDTO createZoomAccount(ZoomAccountDTO zoomAccount){
           return mapper.toDTO(repository.save(mapper.toEntity(zoomAccount)));
    }
    @Transactional("ptm")
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "zoomAccount", key = "#id"),
                    @CacheEvict(cacheNames = "zoomAccounts", allEntries = true)
            })
    public ZoomAccountDTO updateZoomAccount(ZoomAccountDTO zoomAccount, String id) {
        checkAccountNameIsUniqueForGivenType(zoomAccount);
//        calendarService.updateAccount(multipartFile,zoomAccount.getAccountMail());
        return mapper.toDTO(repository.save(mapper.toEntity(zoomAccount)));
    }

    public ZoomAccountDTO findSuitableAccount(long start, long end, MeetingProviderDTO provider){
        return mapper.toDTO(accountManager.getSuitableAccount(start, end, provider));
    }
    
    public List<ZoomAccountDTO> getAll(){
        return mapper.toDTOList(repository.findAllByMeetingProviderType(MeetingProviderTypeEnum.ZOOM));
    }
    @Cacheable(cacheNames = "zoomAccount", key = "#id", unless = "#result == null")
    public ZoomAccountDTO findById(String id) {
        ProviderAccount providerAccount = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Account Not Found", Collections.singleton("zoomAccount")));
        return mapper.toDTO(providerAccount);
    }

    public void checkAccountNameIsUniqueForGivenType(ZoomAccountDTO zoomAccountDTO){
        Optional<ProviderAccount> providerAccount =
                repository.findByAccountMailAndMeetingProviderTypeAndIdNot(zoomAccountDTO.getAccountMail(), ZOOM,
                        zoomAccountDTO.getId() == null ? "-1" : zoomAccountDTO.getId());
        if(providerAccount.isPresent()){
            throw new NotUniqueException("Account name with this type already in use",Collections.singleton("accountMail"));
        }
    }
}
