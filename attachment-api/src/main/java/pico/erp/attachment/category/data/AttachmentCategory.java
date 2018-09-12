package pico.erp.attachment.category.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AttachmentCategory {

  AttachmentCategoryId getId();

  String getName();

  @Getter
  @AllArgsConstructor
  class AttachmentCategoryImpl implements AttachmentCategory {

    AttachmentCategoryId id;

    String name;

  }


}
