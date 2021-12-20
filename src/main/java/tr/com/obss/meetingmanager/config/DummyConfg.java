//package tr.com.obss.meetingmanager.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import tr.com.obss.meetingmanager.entity.Meeting;
//import tr.com.obss.meetingmanager.entity.MeetingProvider;
//import tr.com.obss.meetingmanager.repository.MeetingProviderRepository;
//import tr.com.obss.meetingmanager.repository.MeetingRepository;
//
//import javax.annotation.PostConstruct;
//import java.rmi.server.UID;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Map;
//import java.util.UUID;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;
//
//@Service
//@RequiredArgsConstructor
//public class DummyConfg implements ApplicationListener<ContextRefreshedEvent> {
//    private final MeetingRepository repository;
//    private final MeetingProviderRepository providerRepository;
//
//    @Transactional("ptm")
//    public void init(){
//        for (int i = 0; i< 10 ;i++){
//            MeetingProvider provider = new MeetingProvider();
//            provider.setId(String.valueOf(i));
//            provider.setName(i+".provider");
//            provider.setAccounts(Map.of(i+".account","test@gmail.com"));
//            provider.setConferenceType(POOL);
//            provider.setUserRoleGroup("admin");
//            providerRepository.save(provider);
//        }
//        Map<String, MeetingProvider> providerMap = providerRepository.findAll().stream().collect(Collectors.toMap(MeetingProvider::getId, Function.identity()));
//        int providerCounter = -1;
//        for(int i = 0; i< 100000;i++){
//            providerCounter ++;
//            Meeting m = new Meeting();
//            MeetingProvider provider = providerMap.get(String.valueOf(i) );
//            m.setMeetingProvider(provider);
//            m.setId(UUID.randomUUID().toString());
//            long start = Instant.now().plus(i, ChronoUnit.HOURS).toEpochMilli();
//            long end = Instant.now().plus(i+1, ChronoUnit.HOURS).toEpochMilli();
//                    m.setOrganizer("Admin");
//            m.setDescription(i+". meeting");
//            m.setTitle(i+". title");
//            m.setStartDate(start);
//            m.setEndDate(end);
//            if(providerCounter ==9){
//                providerCounter = 0;
//            }
//            repository.save(m);
//        }
//
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        init();
//    }
//}
