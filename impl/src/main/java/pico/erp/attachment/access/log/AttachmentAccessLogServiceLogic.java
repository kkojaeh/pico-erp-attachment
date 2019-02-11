package pico.erp.attachment.access.log;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@Service
@Public
@Transactional
@Validated
public class AttachmentAccessLogServiceLogic implements AttachmentAccessLogService {

  @Autowired
  private AttachmentAccessLogRepository attachmentAccessLogRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AttachmentAccessLogMapper mapper;

  @Override
  public void create(AttachmentAccessLogRequests.CreateRequest request) {
    val log = new AttachmentAccessLog();
    val response = log.apply(mapper.map(request));
    attachmentAccessLogRepository.create(log);
    eventPublisher.publishEvents(response.getEvents());
  }

}
