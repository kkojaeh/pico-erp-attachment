package pico.erp.attachment.domain;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.shared.event.Event;

public interface AttachmentMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    AttachmentCategory category;

    boolean multiple;

  }

  @Data
  class DeleteRequest {

    boolean force;

  }

  @Data
  class CopyRequest {

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

  @Value
  class CopyResponse {

    Attachment copied;

    Collection<Event> events;

  }

  @Data
  class ClearRequest {

  }

  @Value
  class ClearResponse {

    Collection<Event> events;

  }

  @Data
  class AccessRequest {

  }

  @Value
  class AccessResponse {

    Collection<Event> events;

  }


}
