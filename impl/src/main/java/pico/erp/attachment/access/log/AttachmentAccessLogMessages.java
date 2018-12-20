package pico.erp.attachment.access.log;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.item.AttachmentItem;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;

public interface AttachmentAccessLogMessages {

  @Data
  class CreateRequest {

    AttachmentItem item;

    @Valid
    @NotNull
    AttachmentAccessTypeKind accessType;

    Auditor accessor;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }


}
