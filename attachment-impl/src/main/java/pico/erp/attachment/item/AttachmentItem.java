package pico.erp.attachment.item;

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
import pico.erp.attachment.Attachment;
import pico.erp.attachment.AttachmentExceptions;
import pico.erp.attachment.item.AttachmentItemMessages.RecoverResponse;
import pico.erp.attachment.item.data.AttachmentItemId;
import pico.erp.attachment.item.data.AttachmentItemInfo;
import pico.erp.attachment.storage.data.AttachmentStorageKey;
import pico.erp.shared.data.Auditor;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class AttachmentItem implements Serializable, AttachmentItemInfo {

  private static final long serialVersionUID = 1L;

  @Id
  AttachmentItemId id;

  AttachmentStorageKey storageKey;

  Attachment attachment;

  String name;

  String contentType;

  long contentLength;

  boolean deleted;

  Auditor createdBy;

  OffsetDateTime createdDate;

  OffsetDateTime deletedDate;

  OffsetDateTime lastAccessedDate;

  public AttachmentItem() {
    this.deleted = false;
  }

  public AttachmentItemMessages.DirectAccessResponse apply(
    AttachmentItemMessages.DirectAccessRequest request) {
    if (attachment.isDeleted() || deleted) {
      throw new AttachmentExceptions.CannotAccessException();
    }
    lastAccessedDate = OffsetDateTime.now();
    return new AttachmentItemMessages.DirectAccessResponse(
      Arrays.asList(new AttachmentItemEvents.DirectAccessEvent(this.id, request.getAccessor()))
    );
  }

  public AttachmentItemMessages.UriAccessResponse apply(
    AttachmentItemMessages.UriAccessRequest request) {
    if (attachment.isDeleted() || deleted) {
      throw new AttachmentExceptions.CannotAccessException();
    }
    lastAccessedDate = OffsetDateTime.now();
    return new AttachmentItemMessages.UriAccessResponse(
      Arrays.asList(new AttachmentItemEvents.UriAccessEvent(this.id, request.getAccessor()))
    );
  }

  public AttachmentItemMessages.CreateResponse apply(AttachmentItemMessages.CreateRequest request) {
    this.id = AttachmentItemId.generate();
    this.attachment = request.getAttachment();
    this.name = request.getName();
    this.contentLength = request.getContentLength();
    this.contentType = request.getContentType();
    this.storageKey = request.getAttachmentStorageStrategy().save(this, request.getInputStream());
    return new AttachmentItemMessages.CreateResponse(
      Arrays.asList(new AttachmentItemEvents.CreatedEvent(this.id))
    );
  }

  public AttachmentItemMessages.DeleteResponse apply(AttachmentItemMessages.DeleteRequest request) {
    if (deleted) {
      throw new AttachmentItemExceptions.CannotDeleteException();
    }
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new AttachmentItemMessages.DeleteResponse(
      Arrays.asList(new AttachmentItemEvents.DeletedEvent(this.id, request.isForce()))
    );
  }

  public AttachmentItemMessages.RecoverResponse apply(
    AttachmentItemMessages.RecoverRequest request) {
    if (attachment.isDeleted()) {
      throw new AttachmentExceptions.CannotModifyException();
    }

    if (!deleted) {
      throw new AttachmentItemExceptions.CannotRecoverException();
    }
    deleted = false;
    deletedDate = null;
    return new RecoverResponse(
      Arrays.asList(new AttachmentItemEvents.RecoveredEvent(this.id))
    );
  }

  public AttachmentItemMessages.CopyResponse apply(AttachmentItemMessages.CopyRequest request) {
    if (attachment.isDeleted()) {
      throw new AttachmentExceptions.CannotModifyException();
    }

    val copied = this.toBuilder().build();
    copied.id = AttachmentItemId.generate();
    copied.storageKey = request.getAttachmentStorageStrategy().copy(this.storageKey);
    return new AttachmentItemMessages.CopyResponse(copied,
      Arrays.asList(new AttachmentItemEvents.CopiedEvent(this.id, copied.id)));
  }

  public AttachmentItemMessages.ClearResponse apply(AttachmentItemMessages.ClearRequest request) {
    return new AttachmentItemMessages.ClearResponse(Collections.emptyList());
  }

}
