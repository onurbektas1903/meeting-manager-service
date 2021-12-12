package tr.com.obss.meetingmanager.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tr.com.common.dto.DomainMessage;


@Component
@RequiredArgsConstructor
public class KafkaMessageListener {
    @KafkaListener(
            topics = "onur",
            containerFactory = "customKafkaListenerContainerFactory")
    public void listenNotificationMessages(@Payload DomainMessage message){
    System.out.println(message);
    }
}
