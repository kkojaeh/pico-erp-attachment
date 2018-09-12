package pico.erp.attachment.jpa;

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
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.item.AttachmentItem;
import pico.erp.attachment.item.AttachmentItemRepository;
import pico.erp.attachment.item.data.AttachmentItemId;

@Repository
interface AttachmentItemEntityRepository extends
  CrudRepository<AttachmentItemEntity, AttachmentItemId> {

  @Query("SELECT ai FROM AttachmentItem ai JOIN ai.attachment a WHERE a.id = :attachmentId ORDER BY a.createdDate ASC")
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
  private AttachmentJpaMapper mapper;

  @Override
  public AttachmentItem create(AttachmentItem item) {
    val entity = mapper.map(item);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(AttachmentItemId id) {
    repository.delete(id);
  }

  @Override
  public Stream<AttachmentItem> findAllBy(AttachmentId attachmentId) {
    return repository.findAllBy(attachmentId)
      .map(mapper::map);
  }

  @Override
  public boolean exists(AttachmentItemId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<AttachmentItem> findAllDeletedBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllDeletedBeforeThan(fixedDate)
      .map(mapper::map);
  }

  @Override
  public Optional<AttachmentItem> findBy(AttachmentItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(AttachmentItem item) {
    val entity = repository.findOne(item.getId());
    mapper.pass(mapper.map(item), entity);
    repository.save(entity);
  }
}
