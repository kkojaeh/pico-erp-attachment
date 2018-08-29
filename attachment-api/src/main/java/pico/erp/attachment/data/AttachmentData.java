package pico.erp.attachment.data;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.shared.data.Auditor;

@Data
public class AttachmentData {

  AttachmentId id;

  AttachmentCategoryId categoryId;

  String categoryName;

  boolean multiple;

  Auditor createdBy;

  OffsetDateTime lastAccessedDate;

}
