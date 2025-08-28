package cv.igrp.platform.process.management.shared.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("sendEmailDelegate")
public class SendEmailDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailDelegate.class);

  private final JavaMailSender mailSender;

  public SendEmailDelegate(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void execute(DelegateExecution execution) {
    LOGGER.info("Entered SendEmailDelegate");

    String to = execution.getVariable("emailTo", String.class);
    String subject = execution.getVariable("emailSubject", String.class);
    String body = execution.getVariable("emailBody", String.class);
    String from = execution.getVariable("emailFrom", String.class);

    if (to == null) {
      LOGGER.warn("'emailTo' variable is null. Skipping email sending.");
    }

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setFrom(from == null ? "no-reply@return2sender.ie" : from);
    message.setSubject(subject);
    message.setText(body);

    LOGGER.info("Sending email to: {}", to);
    mailSender.send(message);
    LOGGER.info("Email successfully sent to: {}", to);
  }

}
