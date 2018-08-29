package pico.erp.attachment.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.attachment.AttachmentEvents.CopiedEvent;
import pico.erp.attachment.AttachmentEvents.DeletedEvent;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.shared.data.Auditor;

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString
public class Attachment implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 아이디
   */
  @Id
  AttachmentId id;

  boolean multiple;

  AttachmentCategory category;

  boolean deleted;

  OffsetDateTime deletedDate;

  Auditor createdBy;

  OffsetDateTime createdDate;

  OffsetDateTime lastAccessedDate;

  public Attachment() {
    deleted = false;
  }

  public AttachmentMessages.CreateResponse apply(AttachmentMessages.CreateRequest request) {
    id = AttachmentId.generate();
    category = request.getCategory();
    multiple = request.isMultiple();
    deleted = false;
    return new AttachmentMessages.CreateResponse(Collections.emptyList());
  }

  public AttachmentMessages.DeleteResponse apply(AttachmentMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new AttachmentMessages.DeleteResponse(
      Arrays.asList(new DeletedEvent(this.id, request.isForce()))
    );
  }

  public AttachmentMessages.CopyResponse apply(AttachmentMessages.CopyRequest request) {
    val copied = new Attachment();
    copied.id = AttachmentId.generate();
    copied.category = this.category;
    copied.multiple = this.multiple;

    return new AttachmentMessages.CopyResponse(copied,
      Arrays.asList(new CopiedEvent(this.id, copied.id)));
  }

  public AttachmentMessages.ClearResponse apply(AttachmentMessages.ClearRequest request) {
    return new AttachmentMessages.ClearResponse(Collections.emptyList());
  }

  public AttachmentMessages.AccessResponse apply(AttachmentMessages.AccessRequest request) {
    lastAccessedDate = OffsetDateTime.now();
    return new AttachmentMessages.AccessResponse(Collections.emptyList());
  }

}
