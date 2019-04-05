package pico.erp.attachment;

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
  private AttachmentMapper mapper;

  @Override
  public Attachment create(Attachment attachment) {
    val entity = mapper.entity(attachment);
    val created = repository.save(entity);
    return mapper.domain(created);
  }

  @Override
  public void deleteBy(AttachmentId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(AttachmentId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<Attachment> findAllDeletedBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllDeletedBeforeThan(fixedDate)
      .map(mapper::domain);
  }

  @Override
  public Optional<Attachment> findBy(AttachmentId id) {
    return repository.findById(id)
      .map(mapper::domain);
  }

  @Override
  public void update(Attachment attachment) {
    val entity = repository.findById(attachment.getId()).get();
    mapper.pass(mapper.entity(attachment), entity);
    repository.save(entity);
  }
}
