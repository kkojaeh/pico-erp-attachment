package pico.erp.attachment.access.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.item.AttachmentItemEvents;

@Component
public class AttachmentAccessLogEventListener {

  private static final String LISTENER_NAME = "listener.attachment-access-log-event-listener";

  @Autowired
  AttachmentAccessLogService attachmentAccessLogService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + AttachmentItemEvents.DirectAccessEvent.CHANNEL)
  public void onAttachmentItemDirectAccess(AttachmentItemEvents.DirectAccessEvent event) {
    attachmentAccessLogService.create(
      AttachmentAccessLogRequests.CreateRequest.builder()
        .itemId(event.getAttachmentItemId())
        .accessType(AttachmentAccessTypeKind.DIRECT)
        .accessor(event.getAccessor())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + AttachmentItemEvents.UriAccessEvent.CHANNEL)
  public void onAttachmentItemUriAccess(AttachmentItemEvents.UriAccessEvent event) {
    attachmentAccessLogService.create(
      AttachmentAccessLogRequests.CreateRequest.builder()
        .itemId(event.getAttachmentItemId())
        .accessType(AttachmentAccessTypeKind.URI)
        .accessor(event.getAccessor())
        .build()
    );
  }

}
