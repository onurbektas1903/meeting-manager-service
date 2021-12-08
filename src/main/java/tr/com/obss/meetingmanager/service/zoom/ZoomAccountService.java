package tr.com.obss.meetingmanager.service.zoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.exception.NotFoundException;
import tr.com.obss.meetingmanager.mapper.zoom.ZoomMapper;
import tr.com.obss.meetingmanager.repository.ProviderAccountRepository;
import tr.com.obss.meetingmanager.service.ProviderAccountManagerService;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.ZOOM;

@Service
@RequiredArgsConstructor
public class ZoomAccountService {
    private final ProviderAccountRepository repository;
    private final ZoomMapper mapper;
    private final ProviderAccountManagerService accountManager;
    public ZoomAccountDTO createZoomAccount(ZoomAccountDTO zoomAccount){
           return mapper.toDTO(repository.save(mapper.toEntity(zoomAccount)));
    }
    @Transactional
    public ZoomAccountDTO findSuitableAccount(long start, long end, MeetingProviderDTO provider){
        return mapper.toDTO(accountManager.getSuitableAccount(start, end, provider));
    }
    public List<ZoomAccountDTO> getAllActiveAccounts(){
        return mapper.toDTOList(repository.findAllByMeetingProviderTypeAndIsActive(ZOOM,true));
    }
    public List<ZoomAccountDTO> getAll(){
        return mapper.toDTOList(repository.findAllByMeetingProviderType(ZOOM));
    }

    public ZoomAccountDTO findById(String id) {
        ProviderAccount providerAccount = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Account Not Found", Collections.singleton("zoomAccount")));
        return mapper.toDTO(providerAccount);
    }
}
