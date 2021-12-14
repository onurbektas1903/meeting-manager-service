package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoomAccountService {
    private final ProviderAccountRepository repository;
    private final ZoomMapper mapper;
    private final ProviderAccountManagerService accountManager;
    @Transactional("ptm")
    public ZoomAccountDTO createZoomAccount(ZoomAccountDTO zoomAccount){
           return mapper.toDTO(repository.save(mapper.toEntity(zoomAccount)));
    }
    public ZoomAccountDTO findSuitableAccount(long start, long end, MeetingProviderDTO provider){
        return mapper.toDTO(accountManager.getSuitableAccount(start, end, provider));
    }
    public List<ZoomAccountDTO> getAllActiveAccounts(){
        return mapper.toDTOList(repository.findAllByMeetingProviderTypeAndIsActive(MeetingProviderTypeEnum.ZOOM,true));
    }
    public List<ZoomAccountDTO> getAll(){
        return mapper.toDTOList(repository.findAllByMeetingProviderType(MeetingProviderTypeEnum.ZOOM));
    }

    public ZoomAccountDTO findById(String id) {
        ProviderAccount providerAccount = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Account Not Found", Collections.singleton("zoomAccount")));
        return mapper.toDTO(providerAccount);
    }
}
