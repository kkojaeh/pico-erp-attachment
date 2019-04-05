package pico.erp.attachment.item;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.shared.data.Auditor;

@Data
public class AttachmentItemData {

  AttachmentItemId id;

  String name;

  String contentType;

  long contentLength;

  Auditor createdBy;

  OffsetDateTime createdDate;

  OffsetDateTime deletedDate;

  boolean deleted;

}
