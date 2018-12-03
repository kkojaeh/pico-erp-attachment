package pico.erp.attachment.access.log;

import java.time.OffsetDateTime;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.item.AttachmentItemId;
import pico.erp.shared.data.Auditor;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
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

  public AttachmentAccessLogMessages.CreateResponse apply(
    AttachmentAccessLogMessages.CreateRequest request) {
    val item = request.getItem();
    this.attachmentId = item.getAttachment().getId();
    this.attachmentItemId = item.getId();
    this.name = item.getName();
    this.contentType = item.getContentType();
    this.contentLength = item.getContentLength();
    this.accessType = request.getAccessType();
    this.accessor = request.getAccessor();
    this.accessedDate = OffsetDateTime.now();
    return new AttachmentAccessLogMessages.CreateResponse(Collections.emptyList());
  }

}
