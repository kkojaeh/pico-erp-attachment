package pico.erp.attachment.access.log;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface AttachmentAccessLogEntityRepository extends
  CrudRepository<AttachmentAccessLogEntity, Long> {

}

@Repository
@Transactional
public class AttachmentAccessLogRepositoryJpa implements AttachmentAccessLogRepository {

  @Autowired
  private AttachmentAccessLogEntityRepository repository;

  @Autowired
  private AttachmentAccessLogMapper mapper;

  @Override
  public AttachmentAccessLog create(AttachmentAccessLog log) {
    val entity = mapper.jpa(log);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }
}
