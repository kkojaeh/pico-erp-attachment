package pico.erp.attachment;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.attachment.access.log.AttachmentAccessLog;
import pico.erp.attachment.access.log.AttachmentAccessLogRepository;
import pico.erp.attachment.item.AttachmentItemEvents;
import pico.erp.attachment.item.AttachmentItemExceptions;
import pico.erp.attachment.item.AttachmentItemRepository;
import pico.erp.shared.event.EventPublisher;

@Component
@Transactional
public class AttachmentEventListener {

  private static final String LISTENER_NAME = "listener.attachment-event-listener";

  @Autowired
  private AttachmentRepository attachmentRepository;

  @Autowired
  private AttachmentItemRepository attachmentItemRepository;

  @Autowired
  private AttachmentAccessLogRepository attachmentAccessLogRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "." + AttachmentItemEvents.DirectAccessEvent.CHANNEL)
  public void onAttachmentItemDirectAccess(AttachmentItemEvents.DirectAccessEvent event) {
    val item = attachmentItemRepository.findBy(event.getAttachmentItemId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    attachmentAccessLogRepository.create(
      AttachmentAccessLog.byDirect(item, event.getAccessor())
    );
    val attachment = item.getAttachment();
    val response = attachment.apply(new AttachmentMessages.AccessRequest());
    attachmentRepository.update(attachment);
    eventPublisher.publishEvents(response.getEvents());
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + AttachmentItemEvents.UriAccessEvent.CHANNEL)
  public void onAttachmentItemUriAccess(AttachmentItemEvents.UriAccessEvent event) {
    val item = attachmentItemRepository.findBy(event.getAttachmentItemId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    attachmentAccessLogRepository.create(
      AttachmentAccessLog.byUri(item, event.getAccessor())
    );
    val attachment = item.getAttachment();
    val response = attachment.apply(new AttachmentMessages.AccessRequest());
    attachmentRepository.update(attachment);
    eventPublisher.publishEvents(response.getEvents());
  }

}
