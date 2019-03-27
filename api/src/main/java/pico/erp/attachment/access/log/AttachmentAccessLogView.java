package pico.erp.attachment.access.log;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.item.AttachmentItemId;
import pico.erp.shared.data.Auditor;

@Data
public class AttachmentAccessLogView {

  Long id;

  AttachmentId attachmentId;

  AttachmentItemId attachmentItemId;

  String name;

  String contentType;

  long contentLength;

  AttachmentAccessTypeKind accessType;

  Auditor accessor;

  LocalDateTime accessedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Filter {

    AttachmentId attachmentId;

    AttachmentItemId attachmentItemId;

  }

}
