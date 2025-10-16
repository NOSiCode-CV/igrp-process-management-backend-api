package cv.igrp.platform.process.management.shared.delegates.mail;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("sendEmailDelegate")
public class SendEmailDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailDelegate.class);

  private final JavaMailSender mailSender;
  private final String defaultFrom;

  public SendEmailDelegate(JavaMailSender mailSender,
                           @Value("${igrp.mail.default.from}") String defaultFrom) {
    this.mailSender = mailSender;
    this.defaultFrom = defaultFrom;
  }

  @Override
  public void execute(DelegateExecution execution) {
    LOGGER.info("Entered SendEmailDelegate");

    String to = execution.getVariable("emailTo", String.class);
    String subject = execution.getVariable("emailSubject", String.class);
    String body = execution.getVariable("emailBody", String.class);
    String from = execution.getVariable("emailFrom", String.class);

    validate(to, subject, body);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setFrom(from == null ? defaultFrom : from);
    message.setSubject(subject);
    message.setText(body);

    mailSender.send(message);
    LOGGER.info("Email successfully sent to: {}", to);
  }

  private void validate(String to, String subject, String body) {
    if (to == null || to.isBlank()) {
      throw new IllegalArgumentException("'emailTo' is required.");
    }
    if (subject == null || subject.isBlank()) {
      throw new IllegalArgumentException("'emailSubject' is required.");
    }
    if (body == null || body.isBlank()) {
      throw new IllegalArgumentException("'emailBody' is required.");
    }
  }

}
