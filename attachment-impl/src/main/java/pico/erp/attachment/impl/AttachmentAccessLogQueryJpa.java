package pico.erp.attachment.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.attachment.AttachmentAccessLogQuery;
import pico.erp.attachment.data.AttachmentAccessLogView;
import pico.erp.attachment.impl.jpa.QAttachmentAccessLogEntity;
import pico.erp.shared.Public;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class AttachmentAccessLogQueryJpa implements AttachmentAccessLogQuery {

  private final QAttachmentAccessLogEntity log = QAttachmentAccessLogEntity.attachmentAccessLogEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<AttachmentAccessLogView> retrieve(AttachmentAccessLogView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<AttachmentAccessLogView>(entityManager);
    val select = Projections.bean(AttachmentAccessLogView.class,
      log.id,
      log.attachmentId,
      log.attachmentItemId,
      log.name,
      log.contentType,
      log.contentLength,
      log.accessType,
      log.accessor,
      log.accessedDate
    );
    query.select(select);
    query.from(log);
    val builder = new BooleanBuilder();
    if (filter.getAttachmentId() != null) {
      builder.and(
        log.attachmentId.eq(filter.getAttachmentId())
      );
    }
    if (filter.getAttachmentItemId() != null) {
      builder.and(
        log.attachmentItemId.eq(filter.getAttachmentItemId())
      );
    }
    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
