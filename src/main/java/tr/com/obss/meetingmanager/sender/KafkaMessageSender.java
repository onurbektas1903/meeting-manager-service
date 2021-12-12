package tr.com.obss.meetingmanager.sender;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import tr.com.common.dto.DomainMessage;
import tr.com.common.enums.ActionTypeEnum;

@Service
@RequiredArgsConstructor
public class KafkaMessageSender {
    private final KafkaTemplate<String, DomainMessage> kafkaNotificationTemplate;

    public <T> ListenableFuture<SendResult<String, DomainMessage>> send(
            String topic, T data, ActionTypeEnum actionType) {
        //TODO fix key
        ProducerRecord<String, DomainMessage> producerRecord =
                new ProducerRecord<>(topic, "1", new DomainMessage(actionType, data));
        return this.kafkaNotificationTemplate.send(producerRecord);
    }
    
    public <T> ListenableFuture<SendResult<String, DomainMessage>> sendCreated(String topic, T data) {
        return send(topic, data, ActionTypeEnum.CREATED);
    }

    public <T> ListenableFuture<SendResult<String, DomainMessage>> sendUpdated(String topic, T data) {
        return send(topic, data, ActionTypeEnum.UPDATED);
    }

    public <T> ListenableFuture<SendResult<String, DomainMessage>> sendUpserted(String topic, T data) {
        return send(topic, data, ActionTypeEnum.UPSERTED);
    }

    public <T> ListenableFuture<SendResult<String, DomainMessage>> sendDeleted(String topic, T data) {
        return send(topic, data, ActionTypeEnum.DELETED);
    }
}
