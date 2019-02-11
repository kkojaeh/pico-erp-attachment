package pico.erp.attachment.item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.AttachmentImageData;
import pico.erp.attachment.item.AttachmentItemRequests.CopyRequest;
import pico.erp.attachment.item.AttachmentItemRequests.CreateRequest;
import pico.erp.attachment.item.AttachmentItemRequests.DeleteRequest;
import pico.erp.attachment.item.AttachmentItemRequests.DirectAccessRequest;
import pico.erp.attachment.item.AttachmentItemRequests.GetThumbnailRequest;
import pico.erp.attachment.item.AttachmentItemRequests.RecoverRequest;
import pico.erp.attachment.item.AttachmentItemRequests.UriAccessRequest;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@Service
@Public
@Transactional
@Validated
@SuppressWarnings("Duplicates")
public class AttachmentItemServiceLogic implements AttachmentItemService {


  @Autowired
  private AttachmentItemRepository attachmentItemRepository;

  @Autowired
  private AttachmentItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Lazy
  @Autowired
  private AttachmentStorageStrategy attachmentStorageStrategy;

  @Setter
  @Value("${attachment.no-thumbnail.location}")
  private Resource noThumbnail;

  @Setter
  @Value("${attachment.no-thumbnail.content-type}")
  private String noThumbnailContentType;

  @Override
  public InputStream access(DirectAccessRequest request) {
    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    attachmentItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
    return attachmentStorageStrategy.load(item.getStorageKey());
  }

  @Override
  public URI access(UriAccessRequest request) {
    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    attachmentItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
    return attachmentStorageStrategy.getUri(item.getStorageKey());
  }

  protected boolean canBeThumbnail(AttachmentItem item) {
    return Optional.ofNullable(item.getContentType())
      .map(contentType -> contentType.startsWith("image/"))
      .orElse(false);
  }

  private void clear(AttachmentItem item) {
    if (attachmentStorageStrategy.exists(item.getStorageKey())) {
      attachmentStorageStrategy.remove(item.getStorageKey());
    }
    if (attachmentItemRepository.exists(item.getId())) {
      attachmentItemRepository.deleteBy(item.getId());
    }
  }

  @Override
  public AttachmentItemData create(CreateRequest request) {
    val item = new AttachmentItem();
    val response = item.apply(mapper.map(request));
    val attachment = item.getAttachment();
    if (!attachment.isMultiple()) {
      attachmentItemRepository.findAllBy(attachment.getId()).forEach(sibling -> {
        delete(new DeleteRequest(sibling.getId(), false));
      });
    }
    attachmentItemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(item);
  }

  @Override
  public void delete(DeleteRequest request) {

    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    attachmentItemRepository.update(item);
    if (request.isForce()) {
      clear(item);
    }
    eventPublisher.publishEvents(response.getEvents());

  }

  @Override
  public AttachmentItemData get(AttachmentItemId id) {
    return attachmentItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
  }

  @Override
  public List<AttachmentItemData> getAll(AttachmentId attachmentId) {
    return attachmentItemRepository.findAllBy(attachmentId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public AttachmentImageData getThumbnail(GetThumbnailRequest request) {

    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);

    val thumbnail = canBeThumbnail(item);
    val original = thumbnail ? attachmentStorageStrategy.load(item.getStorageKey())
      : noThumbnail.getInputStream();
    val contentType = thumbnail ? item.getContentType() : noThumbnailContentType;
    val outputStream = new ByteArrayOutputStream();
    Thumbnails.of(original)
      .size(request.getWidth(), request.getHeight())
      .keepAspectRatio(false)
      .toOutputStream(outputStream);
    outputStream.close();
    val bytes = outputStream.toByteArray();
    return AttachmentImageData.builder()
      .contentType(contentType)
      .contentLength(bytes.length)
      .inputStream(new ByteArrayInputStream(bytes))
      .build();
  }

  @Override
  public void recover(RecoverRequest request) {
    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    attachmentItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public AttachmentItemData copy(CopyRequest request) {
    val item = attachmentItemRepository.findBy(request.getId())
      .orElseThrow(AttachmentItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    val copied = attachmentItemRepository.create(response.getCopied());
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(copied);
  }
}
