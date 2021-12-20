package tr.com.obss.meetingmanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.factory.MeetHandlerFactory;

@Service
@RequiredArgsConstructor
public class RollbackService {

    private final MeetHandlerFactory handlerFactory;
    public void processRollback(MeetingDTO meetingDTO){
         handlerFactory.findStrategy(meetingDTO.getMeetingProvider().getMeetingProviderType()).handleRollback(meetingDTO);
    }
}
