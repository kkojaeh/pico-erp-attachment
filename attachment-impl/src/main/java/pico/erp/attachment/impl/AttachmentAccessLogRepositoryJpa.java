package pico.erp.attachment.impl;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.attachment.core.AttachmentAccessLogRepository;
import pico.erp.attachment.domain.AttachmentAccessLog;
import pico.erp.attachment.impl.jpa.AttachmentAccessLogEntity;

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
  private AttachmentJpaMapper mapper;

  @Override
  public AttachmentAccessLog create(AttachmentAccessLog log) {
    val entity = mapper.map(log);
    val created = repository.save(entity);
    return mapper.map(created);
  }
}
