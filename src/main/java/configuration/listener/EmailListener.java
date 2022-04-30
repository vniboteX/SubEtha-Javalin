package configuration.listener;

import domain.EmailInfo;
import service.EmailService;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.helper.SimpleMessageListener;

public class EmailListener implements SimpleMessageListener {

  private final EmailService service;
  private static final Logger log = LoggerFactory.getLogger(EmailListener.class);

  public EmailListener(EmailService service) {
    this.service = service;
  }

  @Override
  public boolean accept(String from, String recipient) {
    log.info("Recipient : " + recipient);
    return recipient != null && recipient.endsWith("@myDomain.com");
  }

  @Override
  public void deliver(String from, String recipient, InputStream data) {
    log.info("From : " + from);
    log.info("Recipient : " + recipient);
    try {
      EmailInfo email = service.extractReceivedEmail(data);
      log.info("Received: {} ", email);
    } catch (Exception e) {
      log.error("An error occurred", e);
    }
  }
}
