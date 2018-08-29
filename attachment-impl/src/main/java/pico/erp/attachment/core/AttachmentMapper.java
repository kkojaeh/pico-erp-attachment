package pico.erp.attachment.core;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.attachment.AttachmentExceptions;
import pico.erp.attachment.AttachmentExceptions.CategoryNotFoundException;
import pico.erp.attachment.AttachmentItemExceptions;
import pico.erp.attachment.AttachmentItemRequests;
import pico.erp.attachment.AttachmentRequests;
import pico.erp.attachment.AttachmentStorageStrategy;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentCategoryId;
import pico.erp.attachment.data.AttachmentData;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentItemData;
import pico.erp.attachment.data.AttachmentItemId;
import pico.erp.attachment.domain.Attachment;
import pico.erp.attachment.domain.AttachmentItem;
import pico.erp.attachment.domain.AttachmentItemMessages;
import pico.erp.attachment.domain.AttachmentMessages.CopyRequest;
import pico.erp.attachment.domain.AttachmentMessages.CreateRequest;
import pico.erp.attachment.domain.AttachmentMessages.DeleteRequest;
import pico.erp.shared.data.Auditor;

@Mapper
public abstract class AttachmentMapper {

  @Autowired
  protected AttachmentItemRepository attachmentItemRepository;

  @Autowired
  protected AttachmentCategoryRepository attachmentCategoryRepository;

  @Autowired
  protected AttachmentRepository attachmentRepository;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected AttachmentStorageStrategy attachmentStorageStrategy;

  protected AttachmentCategory map(AttachmentCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> attachmentCategoryRepository.findBy(id)
        .orElseThrow(CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  protected AttachmentItem map(AttachmentItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> attachmentItemRepository.findBy(id)
        .orElseThrow(AttachmentItemExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected Attachment map(AttachmentId attachmentId) {
    return Optional.ofNullable(attachmentId)
      .map(id -> attachmentRepository.findBy(id)
        .orElseThrow(AttachmentExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "category", source = "categoryId")
  })
  abstract CreateRequest map(AttachmentRequests.CreateRequest request);

  abstract DeleteRequest map(AttachmentRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "attachmentStorageStrategy", expression = "java(attachmentStorageStrategy)"),
    @Mapping(target = "attachment", source = "attachmentId")
  })
  abstract AttachmentItemMessages.CreateRequest map(AttachmentItemRequests.CreateRequest request);

  abstract CopyRequest map(AttachmentRequests.CopyRequest request);

  @Mappings({
    @Mapping(target = "accessor", expression = "java(auditorAware.getCurrentAuditor())")
  })
  abstract AttachmentItemMessages.DirectAccessRequest map(
    AttachmentItemRequests.DirectAccessRequest request);

  abstract AttachmentItemMessages.DeleteRequest map(AttachmentItemRequests.DeleteRequest request);

  abstract AttachmentItemMessages.RecoverRequest map(AttachmentItemRequests.RecoverRequest request);

  @Mappings({
    @Mapping(target = "accessor", expression = "java(auditorAware.getCurrentAuditor())")
  })
  abstract AttachmentItemMessages.UriAccessRequest map(
    AttachmentItemRequests.UriAccessRequest request);

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "categoryName", source = "category.name")
  })
  abstract AttachmentData map(Attachment attachment);

  abstract AttachmentItemData map(AttachmentItem item);


}
