package configuration;

import org.subethamail.smtp.auth.EasyAuthenticationHandlerFactory;
import org.subethamail.smtp.auth.UsernamePasswordValidator;
import org.subethamail.smtp.helper.SimpleMessageListener;
import org.subethamail.smtp.server.SMTPServer;

public class SmtpServerConfig {

  private final SMTPServer smtpServer;

  public SmtpServerConfig(SimpleMessageListener listener) {
    UsernamePasswordValidator authValidator = new SimpleAuthValidatorImpl();
    this.smtpServer = SMTPServer
      .port(25000)
      .simpleMessageListener(listener)
      .backlog(100)  // maximum of pending connection on the socket
      .maxMessageSize(25_000_000) // maximum email size accepted in bytes
      .maxConnections(200) // max of parallel connection accepted by the server
      .maxRecipients(20) // max of email's cc
      .hostName("localhost")
      .requireAuth(true)
      .authenticationHandlerFactory(new EasyAuthenticationHandlerFactory(authValidator))
      .build();
  }

  public void start() {
    this.smtpServer.start();
  }

  public void shutdown() {
    this.smtpServer.stop();
  }
}
