package cv.igrp.platform.process.management.shared.delegates.message.producer;

public interface MessageBrokerSender {

  void send(String destination, String message);

}
