package pico.erp.attachment.impl;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.attachment.AttachmentExceptions;
import pico.erp.attachment.core.AttachmentCategoryRepository;
import pico.erp.attachment.domain.Attachment;
import pico.erp.attachment.domain.AttachmentAccessLog;
import pico.erp.attachment.domain.AttachmentItem;
import pico.erp.attachment.impl.jpa.AttachmentAccessLogEntity;
import pico.erp.attachment.impl.jpa.AttachmentEntity;
import pico.erp.attachment.impl.jpa.AttachmentItemEntity;

@Mapper
public abstract class AttachmentJpaMapper {

  @Autowired
  protected AttachmentCategoryRepository attachmentCategoryRepository;

  protected Attachment map(AttachmentEntity entity) {
    return Attachment.builder()
      .id(entity.getId())
      .multiple(entity.isMultiple())
      .category(
        Optional.ofNullable(entity.getCategoryId())
          .map(id -> attachmentCategoryRepository.findBy(id)
            .orElseThrow(AttachmentExceptions.CategoryNotFoundException::new))
          .orElse(null)
      )
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .lastAccessedDate(entity.getLastAccessedDate())
      .createdBy(entity.getCreatedBy())
      .createdDate(entity.getCreatedDate())
      .build();
  }

  protected AttachmentItem map(AttachmentItemEntity entity) {
    return AttachmentItem.builder()
      .id(entity.getId())
      .attachment(map(entity.getAttachment()))
      .storageKey(entity.getStorageKey())
      .name(entity.getName())
      .contentType(entity.getContentType())
      .contentLength(entity.getContentLength())
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .lastAccessedDate(entity.getLastAccessedDate())
      .createdBy(entity.getCreatedBy())
      .createdDate(entity.getCreatedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  abstract AttachmentEntity map(Attachment attachment);

  abstract AttachmentItemEntity map(AttachmentItem item);

  abstract void pass(AttachmentEntity from, @MappingTarget AttachmentEntity to);

  abstract void pass(AttachmentItemEntity from, @MappingTarget AttachmentItemEntity to);

  abstract AttachmentAccessLogEntity map(AttachmentAccessLog log);

  protected AttachmentAccessLog map(AttachmentAccessLogEntity entity) {
    return AttachmentAccessLog.builder()
      .id(entity.getId())
      .attachmentId(entity.getAttachmentId())
      .attachmentItemId(entity.getAttachmentItemId())
      .name(entity.getName())
      .contentType(entity.getContentType())
      .contentLength(entity.getContentLength())
      .accessType(entity.getAccessType())
      .accessor(entity.getAccessor())
      .accessedDate(entity.getAccessedDate())
      .build();
  }


}
