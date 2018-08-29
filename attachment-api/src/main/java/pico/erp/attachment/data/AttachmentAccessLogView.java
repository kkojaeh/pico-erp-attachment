package pico.erp.attachment.data;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

  OffsetDateTime accessedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Filter {

    AttachmentId attachmentId;

    AttachmentItemId attachmentItemId;

  }

}
