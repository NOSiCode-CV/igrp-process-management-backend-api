package cv.igrp.platform.process.management.shared.delegates.message.producer.kafka;

import cv.igrp.platform.process.management.shared.delegates.message.producer.MessageBrokerSender;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaSender implements MessageBrokerSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void send(String destination, String message) {
    kafkaTemplate.send(destination, message);
    System.out.println("Sent message to Kafka topic [" + destination + "]: " + message);
  }

}
