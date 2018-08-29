package pico.erp.attachment.core;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentCategoryId;

public interface AttachmentCategoryRepository {

  Stream<AttachmentCategory> findAll();

  Optional<AttachmentCategory> findBy(@NotNull AttachmentCategoryId id);

}
