package pico.erp.attachment;

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
import pico.erp.attachment.AttachmentEvents.CopiedEvent;
import pico.erp.attachment.AttachmentEvents.DeletedEvent;
import pico.erp.attachment.category.AttachmentCategory;
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

  LocalDateTime deletedDate;

  Auditor createdBy;

  LocalDateTime createdDate;

  public Attachment() {
    deleted = false;
  }

  public AttachmentMessages.Create.Response apply(AttachmentMessages.Create.Request request) {
    id = request.getId();
    category = request.getCategory();
    multiple = request.isMultiple();
    deleted = false;
    return new AttachmentMessages.Create.Response(Collections.emptyList());
  }

  public AttachmentMessages.Delete.Response apply(AttachmentMessages.Delete.Request request) {
    deleted = true;
    deletedDate = LocalDateTime.now();
    return new AttachmentMessages.Delete.Response(
      Arrays.asList(new DeletedEvent(this.id, request.isForce()))
    );
  }

  public AttachmentMessages.Copy.Response apply(AttachmentMessages.Copy.Request request) {
    val copied = new Attachment();
    copied.id = AttachmentId.generate();
    copied.category = this.category;
    copied.multiple = this.multiple;

    return new AttachmentMessages.Copy.Response(copied,
      Arrays.asList(new CopiedEvent(this.id, copied.id)));
  }

  public AttachmentMessages.Clear.Response apply(AttachmentMessages.Clear.Request request) {
    return new AttachmentMessages.Clear.Response(Collections.emptyList());
  }

}
