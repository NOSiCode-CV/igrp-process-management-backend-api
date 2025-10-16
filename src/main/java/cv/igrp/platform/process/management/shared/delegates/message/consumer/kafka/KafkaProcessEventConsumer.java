package cv.igrp.platform.process.management.shared.delegates.message.consumer.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.consumer.AbstractProcessEventConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@ConditionalOnProperty(value = "message.broker.provider", havingValue = "kafka")
public class KafkaProcessEventConsumer extends AbstractProcessEventConsumer {

  public KafkaProcessEventConsumer(ProcessInstanceService processInstanceService, ObjectMapper objectMapper) {
    super(processInstanceService, objectMapper);
  }

  @KafkaListener(topics = "igrp-process-events", groupId = "igrp-process-event")
  public void listen(String message) {
    handleMessage(message);
  }

}
