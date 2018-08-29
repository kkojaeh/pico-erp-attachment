package pico.erp.attachment.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentCategoryId;

@Getter
@AllArgsConstructor
public class AttachmentCategoryImpl implements AttachmentCategory {

  AttachmentCategoryId id;

  String name;

}
