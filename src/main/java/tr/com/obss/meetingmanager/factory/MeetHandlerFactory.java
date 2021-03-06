package tr.com.obss.meetingmanager.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.common.exceptions.NotFoundException;
import tr.com.obss.meetingmanager.service.MeetingService;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class MeetHandlerFactory {

    private Map<MeetingProviderTypeEnum, MeetingService> serviceMap;

    public MeetHandlerFactory(Set<MeetingService> strategySet){
        createStrategy(strategySet);
    }

    public MeetingService findStrategy(MeetingProviderTypeEnum strategyName) {
        MeetingService meetingService = serviceMap.get(strategyName);
        if(meetingService == null){
            throw new NotFoundException("Strategy Not Found");
        }
        return meetingService;
    }

    private void createStrategy(Set<MeetingService> strategySet) {
        serviceMap = new HashMap<>();
        strategySet.forEach(strategy -> serviceMap.put(strategy.getStrategyName(), strategy));
    }
}
