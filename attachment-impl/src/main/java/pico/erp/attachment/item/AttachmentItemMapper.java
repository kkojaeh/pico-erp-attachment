package pico.erp.attachment.item;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.attachment.Attachment;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.AttachmentMapper;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
import pico.erp.shared.data.Auditor;

@Mapper
public abstract class AttachmentItemMapper {

  @Autowired
  protected AttachmentMapper attachmentMapper;

  @Lazy
  @Autowired
  protected AttachmentItemRepository attachmentItemRepository;

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  protected AttachmentStorageStrategy attachmentStorageStrategy;

  public AttachmentItem jpa(AttachmentItemEntity entity) {
    return AttachmentItem.builder()
      .id(entity.getId())
      .attachment(map(entity.getAttachmentId()))
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
    @Mapping(target = "attachmentId", source = "attachment.id")
  })
  public abstract AttachmentItemEntity jpa(AttachmentItem item);

  @Mappings({
    @Mapping(target = "attachmentStorageStrategy", expression = "java(attachmentStorageStrategy)"),
    @Mapping(target = "attachment", source = "attachmentId")
  })
  public abstract AttachmentItemMessages.CreateRequest map(
    AttachmentItemRequests.CreateRequest request);

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

  public abstract AttachmentItemData map(AttachmentItem item);

  public AttachmentItem map(AttachmentItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> attachmentItemRepository.findBy(id)
        .orElseThrow(AttachmentItemExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected Attachment map(AttachmentId attachmentId) {
    return attachmentMapper.map(attachmentId);
  }

  public abstract void pass(AttachmentItemEntity from, @MappingTarget AttachmentItemEntity to);


}
