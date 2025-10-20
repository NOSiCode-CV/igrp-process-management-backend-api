package cv.igrp.platform.process.management.shared.delegates.message.consumer.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.consumer.AbstractStartProcessConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@ConditionalOnProperty(value = "message.broker.provider", havingValue = "kafka")
public class KafkaStartProcessConsumer extends AbstractStartProcessConsumer {

  public KafkaStartProcessConsumer(ProcessInstanceService processInstanceService, ObjectMapper objectMapper) {
    super(processInstanceService, objectMapper);
  }

  @KafkaListener(topics = "igrp-start-process-events", groupId = "igrp-start-process")
  public void listen(String message) {
    handleMessage(message);
  }

}
