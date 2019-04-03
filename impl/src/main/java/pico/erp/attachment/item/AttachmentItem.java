package pico.erp.attachment.item;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import pico.erp.attachment.storage.AttachmentStorageKey;
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

  LocalDateTime createdDate;

  LocalDateTime deletedDate;

  LocalDateTime lastAccessedDate;

  public AttachmentItem() {
    this.deleted = false;
  }

  public AttachmentItemMessages.DirectAccess.Response apply(
    AttachmentItemMessages.DirectAccess.Request request) {
    if (attachment.isDeleted() || deleted) {
      throw new AttachmentExceptions.CannotAccessException();
    }
    lastAccessedDate = LocalDateTime.now();
    return new AttachmentItemMessages.DirectAccess.Response(
      Arrays.asList(new AttachmentItemEvents.DirectAccessEvent(this.id, request.getAccessor()))
    );
  }

  public AttachmentItemMessages.UriAccess.Response apply(
    AttachmentItemMessages.UriAccess.Request request) {
    if (attachment.isDeleted() || deleted) {
      throw new AttachmentExceptions.CannotAccessException();
    }
    lastAccessedDate = LocalDateTime.now();
    return new AttachmentItemMessages.UriAccess.Response(
      Arrays.asList(new AttachmentItemEvents.UriAccessEvent(this.id, request.getAccessor()))
    );
  }

  public AttachmentItemMessages.Create.Response apply(
    AttachmentItemMessages.Create.Request request) {
    this.id = request.getId();
    this.attachment = request.getAttachment();
    this.name = request.getName();
    this.contentLength = request.getContentLength();
    this.contentType = request.getContentType();
    this.storageKey = request.getAttachmentStorageStrategy().save(this, request.getInputStream());
    return new AttachmentItemMessages.Create.Response(
      Arrays.asList(new AttachmentItemEvents.CreatedEvent(this.id))
    );
  }

  public AttachmentItemMessages.Delete.Response apply(
    AttachmentItemMessages.Delete.Request request) {
    if (deleted) {
      throw new AttachmentItemExceptions.CannotDeleteException();
    }
    deleted = true;
    deletedDate = LocalDateTime.now();
    return new AttachmentItemMessages.Delete.Response(
      Arrays.asList(new AttachmentItemEvents.DeletedEvent(this.id, request.isForce()))
    );
  }

  public AttachmentItemMessages.Recover.Response apply(
    AttachmentItemMessages.Recover.Request request) {
    if (attachment.isDeleted()) {
      throw new AttachmentExceptions.CannotModifyException();
    }

    if (!deleted) {
      throw new AttachmentItemExceptions.CannotRecoverException();
    }
    deleted = false;
    deletedDate = null;
    return new AttachmentItemMessages.Recover.Response(
      Arrays.asList(new AttachmentItemEvents.RecoveredEvent(this.id))
    );
  }

  public AttachmentItemMessages.Copy.Response apply(AttachmentItemMessages.Copy.Request request) {
    if (attachment.isDeleted()) {
      throw new AttachmentExceptions.CannotModifyException();
    }

    val copied = this.toBuilder().build();
    copied.id = AttachmentItemId.generate();
    copied.attachment = request.getToAttachment();
    copied.storageKey = request.getAttachmentStorageStrategy().copy(this.storageKey);
    return new AttachmentItemMessages.Copy.Response(copied,
      Arrays.asList(new AttachmentItemEvents.CopiedEvent(this.id, copied.id)));
  }

  public AttachmentItemMessages.Clear.Response apply(AttachmentItemMessages.Clear.Request request) {
    return new AttachmentItemMessages.Clear.Response(Collections.emptyList());
  }

}
