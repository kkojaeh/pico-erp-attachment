package pico.erp.attachment.access.log;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.attachment.item.AttachmentItem;
import pico.erp.attachment.item.AttachmentItemId;
import pico.erp.attachment.item.AttachmentItemMapper;

@Mapper
public abstract class AttachmentAccessLogMapper {

  @Autowired
  AttachmentItemMapper itemMapper;

  public AttachmentAccessLog jpa(AttachmentAccessLogEntity entity) {
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

  public abstract AttachmentAccessLogEntity jpa(AttachmentAccessLog log);

  @Mappings({
    @Mapping(target = "item", source = "itemId")
  })
  public abstract AttachmentAccessLogMessages.CreateRequest map(
    AttachmentAccessLogServiceLogic.CreateRequest request);

  protected AttachmentItem map(AttachmentItemId itemId) {
    return itemMapper.map(itemId);
  }


}
