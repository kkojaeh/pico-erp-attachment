package pico.erp.attachment.item;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.attachment.AttachmentId;

@Repository
interface AttachmentItemEntityRepository extends
  CrudRepository<AttachmentItemEntity, AttachmentItemId> {

  @Query("SELECT ai FROM AttachmentItem ai WHERE ai.attachmentId = :attachmentId AND ai.deleted = false ORDER BY ai.createdDate ASC")
  Stream<AttachmentItemEntity> findAllBy(
    @Param("attachmentId") AttachmentId attachmentId);

  @Query("SELECT ai FROM AttachmentItem ai WHERE ai.deleted = true AND ai.deletedDate < :fixedDate")
  Stream<AttachmentItemEntity> findAllDeletedBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate);

}

@Repository
@Transactional
public class AttachmentItemRepositoryJpa implements AttachmentItemRepository {

  @Autowired
  private AttachmentItemEntityRepository repository;

  @Autowired
  private AttachmentItemMapper mapper;

  @Override
  public AttachmentItem create(AttachmentItem item) {
    val entity = mapper.jpa(item);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(AttachmentItemId id) {
    repository.deleteById(id);
  }

  @Override
  public Stream<AttachmentItem> findAllBy(AttachmentId attachmentId) {
    return repository.findAllBy(attachmentId)
      .map(mapper::jpa);
  }

  @Override
  public boolean exists(AttachmentItemId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<AttachmentItem> findAllDeletedBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllDeletedBeforeThan(fixedDate)
      .map(mapper::jpa);
  }

  @Override
  public Optional<AttachmentItem> findBy(AttachmentItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(AttachmentItem item) {
    val entity = repository.findById(item.getId()).get();
    mapper.pass(mapper.jpa(item), entity);
    repository.save(entity);
  }
}
