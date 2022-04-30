package domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailInfo {

  private String subject;
  private String senderAddress;
  private String senderName;
  private String recipientAddress;
  private String recipientName;
  private String contentType;
  private List<Attachment> attachments = new ArrayList<>();

}
