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
import pico.erp.attachment.Attachment;
import pico.erp.attachment.AttachmentRepository;
import pico.erp.attachment.data.AttachmentId;

@Repository
interface AttachmentEntityRepository extends CrudRepository<AttachmentEntity, AttachmentId> {

  @Query("SELECT a FROM Attachment a WHERE a.deleted = true AND a.deletedDate < :fixedDate")
  Stream<AttachmentEntity> findAllDeletedBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate);

}

@Repository
@Transactional
public class AttachmentRepositoryJpa implements AttachmentRepository {

  @Autowired
  private AttachmentEntityRepository repository;

  @Autowired
  private AttachmentJpaMapper mapper;

  @Override
  public Attachment create(Attachment attachment) {
    val entity = mapper.map(attachment);
    val created = repository.save(entity);
    return mapper.map(created);
  }

  @Override
  public void deleteBy(AttachmentId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(AttachmentId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<Attachment> findAllDeletedBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllDeletedBeforeThan(fixedDate)
      .map(mapper::map);
  }

  @Override
  public Optional<Attachment> findBy(AttachmentId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(Attachment attachment) {
    val entity = repository.findOne(attachment.getId());
    mapper.pass(mapper.map(attachment), entity);
    repository.save(entity);
  }
}
