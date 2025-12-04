package cv.igrp.platform.process.management.shared.delegates.message.consumer.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.consumer.AbstractStartProcessConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@ConditionalOnProperty(value = "igrp.message.broker.provider", havingValue = "rabbitmq")
public class RabbitStartProcessConsumer extends AbstractStartProcessConsumer {

  public RabbitStartProcessConsumer(ProcessInstanceService processInstanceService,
                                    ObjectMapper objectMapper, JwtDecoder jwtDecoder) {
    super(processInstanceService, objectMapper, jwtDecoder);
  }

  //@RabbitListener(queues = "igrp-start-process-queue")
  public void listen(ConsumerRecord<String, String> record) {
    handleMessage(record);
  }

}
