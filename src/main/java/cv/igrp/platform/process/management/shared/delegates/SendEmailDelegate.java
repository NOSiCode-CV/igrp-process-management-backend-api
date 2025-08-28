package cv.igrp.platform.process.management.shared.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("sendEmailDelegate")
public class SendEmailDelegate implements JavaDelegate {


  private final JavaMailSender mailSender;

  public SendEmailDelegate(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void execute(DelegateExecution execution) {
    String to = (String) execution.getVariable("emailTo");
    String subject = (String) execution.getVariable("emailSubject");
    String body = (String) execution.getVariable("emailBody");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setFrom("no-reply@return2sender.ie");
    message.setSubject(subject != null ? subject : "Default Subject");
    message.setText(body != null ? body : "Hello from Activiti!");

    mailSender.send(message);

    System.out.println("✅ Email sent to: " + to);
  }

}
