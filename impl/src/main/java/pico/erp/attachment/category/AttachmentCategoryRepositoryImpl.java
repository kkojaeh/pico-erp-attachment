package pico.erp.attachment.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kkojaeh.spring.boot.component.Take;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentCategoryRepositoryImpl implements AttachmentCategoryRepository {

  @Take(required = false)
  List<AttachmentCategory> attachmentCategories;

  @Override
  public Stream<AttachmentCategory> findAll() {
    return attachmentCategories.stream();
  }

  @Override
  public Optional<AttachmentCategory> findBy(AttachmentCategoryId id) {
    return attachmentCategories.stream()
      .filter(r -> r.getId().equals(id))
      .findFirst();
  }

}
