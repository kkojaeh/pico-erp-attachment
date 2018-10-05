package pico.erp.attachment;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import pico.erp.attachment.AttachmentExceptions.CategoryNotFoundException;
import pico.erp.attachment.AttachmentMessages.CopyRequest;
import pico.erp.attachment.AttachmentMessages.CreateRequest;
import pico.erp.attachment.AttachmentMessages.DeleteRequest;
import pico.erp.attachment.category.AttachmentCategory;
import pico.erp.attachment.category.AttachmentCategoryId;
import pico.erp.attachment.category.AttachmentCategoryRepository;

@Mapper
public abstract class AttachmentMapper {


  @Autowired
  protected AttachmentCategoryRepository attachmentCategoryRepository;

  @Autowired
  protected AttachmentRepository attachmentRepository;

  protected AttachmentCategory map(AttachmentCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> attachmentCategoryRepository.findBy(id)
        .orElseThrow(CategoryNotFoundException::new)
      )
      .orElse(null);
  }

  public Attachment domain(AttachmentEntity entity) {
    return Attachment.builder()
      .id(entity.getId())
      .multiple(entity.isMultiple())
      .category(
        Optional.ofNullable(entity.getCategoryId())
          .map(id -> attachmentCategoryRepository.findBy(id)
            .orElseThrow(AttachmentExceptions.CategoryNotFoundException::new))
          .orElse(null)
      )
      .deleted(entity.isDeleted())
      .deletedDate(entity.getDeletedDate())
      .lastAccessedDate(entity.getLastAccessedDate())
      .createdBy(entity.getCreatedBy())
      .createdDate(entity.getCreatedDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "category", source = "categoryId")
  })
  public abstract CreateRequest map(AttachmentRequests.CreateRequest request);

  public abstract DeleteRequest map(AttachmentRequests.DeleteRequest request);



  public abstract CopyRequest map(AttachmentRequests.CopyRequest request);


  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "categoryName", source = "category.name")
  })
  public abstract AttachmentData map(Attachment attachment);

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract AttachmentEntity entity(Attachment attachment);

  public Attachment map(AttachmentId attachmentId) {
    return Optional.ofNullable(attachmentId)
      .map(id -> attachmentRepository.findBy(id)
        .orElseThrow(AttachmentExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  public abstract void pass(AttachmentEntity from, @MappingTarget AttachmentEntity to);


}
