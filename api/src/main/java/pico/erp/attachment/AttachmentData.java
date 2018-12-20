package pico.erp.attachment;

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

}
