package pico.erp.attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.item.data.AttachmentItemId;
import pico.erp.shared.event.Event;

public interface AttachmentEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CopiedEvent implements Event {

    public final static String CHANNEL = "event.attachment.copied";

    private AttachmentId originalId;

    private AttachmentId copiedId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.attachment.deleted";

    private AttachmentId attachmentId;

    private boolean force;

    public String channel() {
      return CHANNEL;
    }

  }



  @ToString
  @Value
  class ItemRemovedEvent implements Event {

    public final static String CHANNEL = "event.attachment.item-removed";

    private AttachmentId attachmentId;

    private AttachmentItemId attachmentItemId;

    private boolean force;

    public String channel() {
      return CHANNEL;
    }

  }


}
