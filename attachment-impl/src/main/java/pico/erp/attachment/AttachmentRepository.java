package pico.erp.attachment;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface AttachmentRepository {

  Attachment create(@NotNull Attachment attachment);

  void deleteBy(@NotNull AttachmentId id);

  boolean exists(@NotNull AttachmentId id);

  Stream<Attachment> findAllDeletedBeforeThan(@NotNull OffsetDateTime fixedDate);

  Optional<Attachment> findBy(@NotNull AttachmentId id);

  void update(@NotNull Attachment attachment);


}
