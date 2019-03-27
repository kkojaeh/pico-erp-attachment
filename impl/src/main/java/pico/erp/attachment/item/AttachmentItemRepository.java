package pico.erp.attachment.item;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.AttachmentId;

public interface AttachmentItemRepository {

  AttachmentItem create(@NotNull AttachmentItem item);

  Stream<AttachmentItem> findAllBy(AttachmentId attachmentId);

  void deleteBy(AttachmentItemId id);

  boolean exists(AttachmentItemId id);

  Stream<AttachmentItem> findAllDeletedBeforeThan(LocalDateTime fixedDate);

  Optional<AttachmentItem> findBy(AttachmentItemId id);

  void update(@NotNull AttachmentItem item);


}
