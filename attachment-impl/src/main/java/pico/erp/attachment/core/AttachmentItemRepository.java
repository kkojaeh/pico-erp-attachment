package pico.erp.attachment.core;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentItemId;
import pico.erp.attachment.domain.AttachmentItem;

public interface AttachmentItemRepository {

  AttachmentItem create(@NotNull AttachmentItem item);

  Stream<AttachmentItem> findAllBy(AttachmentId attachmentId);

  void deleteBy(AttachmentItemId id);

  boolean exists(AttachmentItemId id);

  Stream<AttachmentItem> findAllDeletedBeforeThan(OffsetDateTime fixedDate);

  Optional<AttachmentItem> findBy(AttachmentItemId id);

  void update(@NotNull AttachmentItem item);


}
