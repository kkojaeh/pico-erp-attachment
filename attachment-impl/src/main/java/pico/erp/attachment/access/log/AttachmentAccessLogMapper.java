package pico.erp.attachment.access.log;

import org.mapstruct.Mapper;

@Mapper
public abstract class AttachmentAccessLogMapper {

  public AttachmentAccessLog domain(AttachmentAccessLogEntity entity) {
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

  public abstract AttachmentAccessLogEntity entity(AttachmentAccessLog log);

}
