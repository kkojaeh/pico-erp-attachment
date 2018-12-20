package pico.erp.attachment.category;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface AttachmentCategoryRepository {

  Stream<AttachmentCategory> findAll();

  Optional<AttachmentCategory> findBy(@NotNull AttachmentCategoryId id);

}
