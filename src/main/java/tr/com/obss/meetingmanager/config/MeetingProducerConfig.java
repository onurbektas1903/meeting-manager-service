package tr.com.obss.meetingmanager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import tr.com.common.config.KafkaProducerConfig;
import tr.com.common.dto.DomainMessage;

@Configuration
@RequiredArgsConstructor
public class MeetingProducerConfig {
    private final KafkaProducerConfig config;
    @Bean
    public KafkaTemplate<String, DomainMessage> kafkaNotificationTemplate() {
        return new KafkaTemplate<>(config.producerFactory());
    }
}
