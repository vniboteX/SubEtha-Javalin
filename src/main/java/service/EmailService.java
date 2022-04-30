package service;

import domain.Attachment;
import domain.EmailInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  public EmailInfo extractReceivedEmail(InputStream data) throws Exception {
    EmailInfo emailInfo = new EmailInfo();
    List<Attachment> attachments = new ArrayList<>();
    MimeMessage message = this.convertToMimeMessage(data);
    emailInfo.setSubject(message.getSubject());
    emailInfo.setSenderAddress(InternetAddress.toString(message.getFrom()));
    InternetAddress[] recipientAddresses = InternetAddress.parse(InternetAddress.toString(message.getAllRecipients()));
    emailInfo.setRecipientAddress(InternetAddress.toString(recipientAddresses));
    emailInfo.setRecipientName(recipientAddresses[0].getPersonal());
    emailInfo.setContentType(message.getContentType());

    if (emailInfo.getContentType().contains("multipart")) {
      // content may contain attachments
      Multipart multiPart = (Multipart) message.getContent();
      int numberOfParts = multiPart.getCount();
      for (int partCount = 0; partCount < numberOfParts; partCount++) {
        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
        if (part.getFileName() == null) {
          //body of the email
          log.info("body: {}", getText(part));
        } else {
          log.info("attachment: {}", getText(part));
          String fileName = part.getFileName();
          InputStream inputStream = part.getInputStream();
          int size = inputStream.available();
          Attachment attachment = new Attachment(fileName, size, inputStream);
          attachments.add(attachment);
        }
      }
      emailInfo.setAttachments(attachments);
    }

    return emailInfo;
  }

  /**
   * Return the primary text content of the message.
   */
  private String getText(Part p) throws MessagingException, IOException {
    if (p.isMimeType("text/*")) {
      String s = (String) p.getContent();
      p.isMimeType("text/html");
      return s;
    }
    if (p.isMimeType("multipart/alternative")) {
      Multipart mp = (Multipart) p.getContent();
      String text = null;
      for (int i = 0; i < mp.getCount(); i++) {
        Part bp = mp.getBodyPart(i);
        if (bp.isMimeType("text/plain")) {
          if (text == null) {
            text = getText(bp);
          }
        } else if (bp.isMimeType("text/html")) {
          String s = getText(bp);
          if (s != null) {
            return s;
          }
        } else {
          return getText(bp);
        }
      }
      return text;
    } else if (p.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) p.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        String s = getText(mp.getBodyPart(i));
        if (s != null) {
          return s;
        }
      }
    }

    return null;
  }

  public MimeMessage convertToMimeMessage(InputStream data) throws MessagingException {
    Session session = Session.getDefaultInstance(new Properties());
    try {
      return new MimeMessage(session, data);
    } catch (MessagingException e) {
      throw new MessagingException();
    }
  }

}
