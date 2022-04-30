package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.auth.LoginFailedException;
import org.subethamail.smtp.auth.UsernamePasswordValidator;

public class SimpleAuthValidatorImpl implements UsernamePasswordValidator {

  private final String CREDENTIALS_LOGIN = "vnibotex";
  private final String CREDENTIALS_PASSWORD = "password1";
  private static final Logger log = LoggerFactory.getLogger(SimpleAuthValidatorImpl.class);


  @Override
  public void login(String username, String password, MessageContext context) throws LoginFailedException {
    if (CREDENTIALS_LOGIN.equals(username) && CREDENTIALS_PASSWORD.equals(password)) {
      log.info("Authenticated successfully");
    } else {
      log.error("Invalid authentication !");
      throw new LoginFailedException();
    }
  }
}
