package pico.erp.attachment;

import java.util.LinkedList;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.Give;
import kkojaeh.spring.boot.component.Take;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.attachment.AttachmentExceptions.NotFoundException;
import pico.erp.attachment.AttachmentRequests.ClearRequest;
import pico.erp.attachment.AttachmentRequests.CopyRequest;
import pico.erp.attachment.AttachmentRequests.CreateRequest;
import pico.erp.attachment.AttachmentRequests.DeleteRequest;
import pico.erp.attachment.item.AttachmentItem;
import pico.erp.attachment.item.AttachmentItemMessages;
import pico.erp.attachment.item.AttachmentItemRepository;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
import pico.erp.shared.event.Event;
import pico.erp.shared.event.EventPublisher;

@Service
@Give
@Transactional
@Validated
public class AttachmentServiceLogic implements AttachmentService {

  @Autowired
  private AttachmentItemRepository attachmentItemRepository;

  @Autowired
  private AttachmentRepository attachmentRepository;

  @Take(required = false)
  private AttachmentStorageStrategy attachmentStorageStrategy;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AttachmentIconConfiguration attachmentIconConfiguration;

  @Setter
  @Value("${attachment.no-thumbnail.location}")
  private Resource noThumbnail;

  @Setter
  @Value("${attachment.no-thumbnail.content-type}")
  private String noThumbnailContentType;

  @Autowired
  private AttachmentMapper mapper;


  private void clear(Attachment attachment) {
    val events = new LinkedList<Event>();
    val response = attachment.apply(new AttachmentMessages.Clear.Request());
    events.addAll(response.getEvents());
    val items = attachmentItemRepository.findAllBy(attachment.getId()).collect(Collectors.toList());

    items.forEach(item -> {
      val itemResponse = item.apply(new AttachmentItemMessages.Delete.Request(true));
      events.addAll(itemResponse.getEvents());
    });
    items.forEach(item -> {
      if (attachmentStorageStrategy.exists(item.getStorageKey())) {
        attachmentStorageStrategy.remove(item.getStorageKey());
      }
      if (attachmentItemRepository.exists(item.getId())) {
        attachmentItemRepository.deleteBy(item.getId());
      }
    });
    if (attachmentRepository.exists(attachment.getId())) {
      attachmentRepository.deleteBy(attachment.getId());
    }
    eventPublisher.publishEvents(events);
  }

  private void clear(AttachmentItem item) {
    val response = item.apply(new AttachmentItemMessages.Clear.Request());
    if (attachmentStorageStrategy.exists(item.getStorageKey())) {
      attachmentStorageStrategy.remove(item.getStorageKey());
    }
    if (attachmentItemRepository.exists(item.getId())) {
      attachmentItemRepository.deleteBy(item.getId());
    }
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void clear(ClearRequest request) {
    attachmentRepository.findAllDeletedBeforeThan(request.getFixedDate())
      .forEach(this::clear);
    attachmentItemRepository.findAllDeletedBeforeThan(request.getFixedDate()).forEach(this::clear);
  }

  @Override
  public AttachmentData copy(CopyRequest request) {
    val attachment = attachmentRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = attachment.apply(mapper.map(request));
    val copied = attachmentRepository.create(response.getCopied());
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(copied);
  }

  @Override
  public AttachmentData create(CreateRequest request) {
    val attachment = new Attachment();
    val response = attachment.apply(mapper.map(request));
    val created = attachmentRepository.create(attachment);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val attachment = attachmentRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = attachment.apply(mapper.map(request));

    attachmentRepository.update(attachment);
    if (request.isForce()) {
      clear(attachment);
    }
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public AttachmentData get(AttachmentId id) {
    return attachmentRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public AttachmentImageData getIcon(String contentType) {
    return attachmentIconConfiguration.get(contentType);
  }

  @Override
  public boolean isUriSupported() {
    return attachmentStorageStrategy.isUriSupported();
  }

}
