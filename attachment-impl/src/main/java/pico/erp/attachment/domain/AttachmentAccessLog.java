package pico.erp.attachment.domain;

import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.attachment.data.AttachmentAccessTypeKind;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentItemId;
import pico.erp.shared.data.Auditor;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class AttachmentAccessLog {

  Long id;

  AttachmentId attachmentId;

  AttachmentItemId attachmentItemId;

  String name;

  String contentType;

  long contentLength;

  AttachmentAccessTypeKind accessType;

  Auditor accessor;

  OffsetDateTime accessedDate;

  private static AttachmentAccessLog build(AttachmentAccessTypeKind accessType, AttachmentItem item,
    Auditor accessor) {
    return AttachmentAccessLog.builder()
      .attachmentId(item.getAttachment().getId())
      .attachmentItemId(item.getId())
      .name(item.getName())
      .contentType(item.getContentType())
      .contentLength(item.getContentLength())
      .accessType(accessType)
      .accessor(accessor)
      .accessedDate(OffsetDateTime.now())
      .build();
  }

  public static AttachmentAccessLog byDirect(AttachmentItem item,
    Auditor accessor) {
    return build(AttachmentAccessTypeKind.DIRECT, item, accessor);
  }

  public static AttachmentAccessLog byUri(AttachmentItem item,
    Auditor accessor) {
    return build(AttachmentAccessTypeKind.URI, item, accessor);
  }

}
