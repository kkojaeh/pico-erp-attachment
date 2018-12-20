package pico.erp.attachment.access.log;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.item.AttachmentItemId;
import pico.erp.shared.Public;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;

@Service
@Public
@Transactional
@Validated
public class AttachmentAccessLogServiceLogic {

  @Autowired
  private AttachmentAccessLogRepository attachmentAccessLogRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AttachmentAccessLogMapper mapper;

  public void create(CreateRequest request) {
    val log = new AttachmentAccessLog();
    val response = log.apply(mapper.map(request));
    attachmentAccessLogRepository.create(log);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class CreateRequest {

    @Valid
    @NotNull
    AttachmentItemId itemId;

    @NotNull
    Auditor accessor;

    @NotNull
    AttachmentAccessTypeKind accessType;

  }

}
