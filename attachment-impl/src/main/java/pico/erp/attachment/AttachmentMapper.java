package pico.erp.attachment;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.attachment.AttachmentExceptions.CategoryNotFoundException;
import pico.erp.attachment.AttachmentMessages.CopyRequest;
import pico.erp.attachment.AttachmentMessages.CreateRequest;
import pico.erp.attachment.AttachmentMessages.DeleteRequest;
import pico.erp.attachment.category.AttachmentCategoryRepository;
import pico.erp.attachment.category.data.AttachmentCategory;
import pico.erp.attachment.category.data.AttachmentCategoryId;
import pico.erp.attachment.data.AttachmentData;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.item.AttachmentItem;
import pico.erp.attachment.item.AttachmentItemExceptions;
import pico.erp.attachment.item.AttachmentItemMessages;
import pico.erp.attachment.item.AttachmentItemRepository;
import pico.erp.attachment.item.AttachmentItemRequests;
import pico.erp.attachment.item.data.AttachmentItemData;
import pico.erp.attachment.item.data.AttachmentItemId;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
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
  public abstract CreateRequest map(AttachmentRequests.CreateRequest request);

  public abstract DeleteRequest map(AttachmentRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "attachmentStorageStrategy", expression = "java(attachmentStorageStrategy)"),
    @Mapping(target = "attachment", source = "attachmentId")
  })
  public abstract AttachmentItemMessages.CreateRequest map(
    AttachmentItemRequests.CreateRequest request);

  public abstract CopyRequest map(AttachmentRequests.CopyRequest request);

  @Mappings({
    @Mapping(target = "accessor", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract AttachmentItemMessages.DirectAccessRequest map(
    AttachmentItemRequests.DirectAccessRequest request);

  public abstract AttachmentItemMessages.DeleteRequest map(
    AttachmentItemRequests.DeleteRequest request);

  public abstract AttachmentItemMessages.RecoverRequest map(
    AttachmentItemRequests.RecoverRequest request);

  @Mappings({
    @Mapping(target = "accessor", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract AttachmentItemMessages.UriAccessRequest map(
    AttachmentItemRequests.UriAccessRequest request);

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "categoryName", source = "category.name")
  })
  public abstract AttachmentData map(Attachment attachment);

  public abstract AttachmentItemData map(AttachmentItem item);


}
