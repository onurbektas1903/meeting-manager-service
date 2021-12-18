package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.feigns.ZoomServiceClient;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "zoomAccountCache")
public class ZoomAccountService {
    private final ProviderAccountManagerService accountManagerService;
    private final ProviderAccountManagerService providerManager;
    private final ZoomServiceClient zoomServiceClient;
    private final ZoomMapper mapper;

    @Transactional("ptm")
    @CacheEvict(cacheNames = "zoomAccounts", allEntries = true)
    public ZoomAccountDTO createZoomAccount(ZoomAccountDTO zoomAccount){
        accountManagerService.checkAccountNameIsUniqueForGivenType("-1",zoomAccount.getAccountMail(),ZOOM);
        zoomServiceClient.createZoomAccount(zoomAccount);
        return mapper.toDTO(accountManagerService.saveAccount(mapper.toEntity(zoomAccount)));
    }

    @Transactional("ptm")
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "zoomAccount", key = "#id"),
                    @CacheEvict(cacheNames = "zoomAccounts", allEntries = true)
            })
    public ZoomAccountDTO updateZoomAccount(ZoomAccountDTO zoomAccount, String id) {
        ZoomAccountDTO zoomAccountDTO = findById(id);
        accountManagerService.checkAccountNameIsUniqueForGivenType(id,zoomAccount.getAccountMail(),ZOOM);
        zoomServiceClient.updateAccount(zoomAccount.getAccountMail(),zoomAccount);
        return mapper.toDTO(accountManagerService.saveAccount(mapper.toEntity(zoomAccount)));
    }

    @Transactional("ptm")
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "zoomAccount", key = "#id"),
                    @CacheEvict(cacheNames = "zoomAccounts", allEntries = true)
            })
    public ZoomAccountDTO deleteZoomAccount(ZoomAccountDTO zoomAccount, String id) {
        accountManagerService.checkAccountNameIsUniqueForGivenType(id,zoomAccount.getAccountMail(),ZOOM);
        zoomServiceClient.deleteAccount(zoomAccount.getAccountMail(),zoomAccount);
        return mapper.toDTO(accountManagerService.saveAccount(mapper.toEntity(zoomAccount)));
    }

    public ZoomAccountDTO findSuitableAccount(long start, long end, MeetingProviderDTO provider){
        return mapper.toDTO(providerManager.getSuitableAccount(start, end, provider));
    }
    
    public List<ZoomAccountDTO> getAll(){
        return mapper.toDTOList(accountManagerService.findAllByMeetingProviderType(MeetingProviderTypeEnum.ZOOM));
    }
    public List<ZoomAccountDTO> getFreeZoomAccounts(String providerId){
        return mapper.toDTOList(accountManagerService.findFreeProviderAccounts(ZOOM,providerId));
    }

    @Cacheable(cacheNames = "zoomAccount", key = "#id", unless = "#result == null")
    public ZoomAccountDTO findById(String id) {
        return mapper.toDTO(providerManager.findProviderAccountById(id));
    }

}
