package tr.com.obss.meetingmanager.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import tr.com.obss.dto.DomainMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    
    @Bean
    public KafkaTemplate<String, DomainMessage> kafkaNotificationTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    public ProducerFactory<String, DomainMessage> producerFactory() {
        return producerFactory(Collections.emptyMap());
    }

    public ProducerFactory<String, DomainMessage> producerFactory(
            Map<String, Object> configMap) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, "250000");
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "200");
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.LZ4.name);
        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
        configProps.putAll(configMap);

        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
