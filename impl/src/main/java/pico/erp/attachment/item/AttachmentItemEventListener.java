package pico.erp.attachment.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.attachment.AttachmentEvents;

@Component
public class AttachmentItemEventListener {

  private static final String LISTENER_NAME = "listener.attachment-item-event-listener";

  @Autowired
  AttachmentItemService attachmentItemService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + AttachmentEvents.CopiedEvent.CHANNEL)
  public void onAttachmentCopied(AttachmentEvents.CopiedEvent event) {

    attachmentItemService.getAll(event.getOriginalId())
      .forEach(item -> {
        attachmentItemService.copy(
          AttachmentItemRequests.CopyRequest.builder()
            .id(item.getId())
            .toAttachmentId(event.getCopiedId())
            .build()
        );
      });
    ;

  }

}
