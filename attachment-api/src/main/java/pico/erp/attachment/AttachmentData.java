package pico.erp.attachment;

import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.attachment.category.AttachmentCategoryId;
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
