package pico.erp.attachment.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.attachment.item.data.AttachmentItemId;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;

public interface AttachmentItemEvents {


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.deleted";

    private AttachmentItemId attachmentItemId;

    private boolean force;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.created";

    private AttachmentItemId attachmentItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DirectAccessEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.direct-access";

    private AttachmentItemId attachmentItemId;

    private Auditor accessor;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UriAccessEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.uri-access";

    private AttachmentItemId attachmentItemId;

    private Auditor accessor;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class RecoveredEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.recovered";

    private AttachmentItemId attachmentItemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CopiedEvent implements Event {

    public final static String CHANNEL = "event.attachment-item.copied";

    private AttachmentItemId originalId;

    private AttachmentItemId copiedId;

    public String channel() {
      return CHANNEL;
    }

  }

}
