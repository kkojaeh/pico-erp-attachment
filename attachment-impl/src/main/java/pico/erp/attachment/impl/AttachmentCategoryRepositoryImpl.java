package pico.erp.attachment.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import pico.erp.attachment.core.AttachmentCategoryRepository;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentCategoryId;

@Repository
public class AttachmentCategoryRepositoryImpl implements AttachmentCategoryRepository {

  @Lazy
  @Autowired
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
