package tr.com.obss.meetingmanager.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.service.ProviderService;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class MeetProviderHandlerFactory {

    private Map<MeetingProviderTypeEnum, ProviderService> serviceMap;

    public MeetProviderHandlerFactory(Set<ProviderService> strategySet){
        createStrategy(strategySet);
    }

    public ProviderService findStrategy(MeetingProviderTypeEnum strategyName) {
        ProviderService providerService = serviceMap.get(strategyName);
        if(providerService == null){
            //TODO customize this exception
            throw new RuntimeException("Strategy Not Found");
        }
        return providerService;
    }

    private void createStrategy(Set<ProviderService> strategySet) {
        serviceMap = new HashMap<>();
        strategySet.forEach(strategy -> serviceMap.put(strategy.getStrategyName(), strategy));
    }
}
