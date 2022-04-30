import configuration.listener.EmailListener;
import service.EmailService;
import configuration.SmtpServerConfig;
import io.javalin.Javalin;

public class EmailsReceiverApplication {

  public static void main(String[] args) {
    Javalin app = Javalin.create().start(7070);

    EmailService emailService = new EmailService();
    SmtpServerConfig smtpServer = new SmtpServerConfig(new EmailListener(emailService));
    smtpServer.start();

    Runtime.getRuntime().addShutdownHook(new Thread(smtpServer::shutdown));

  }

}
