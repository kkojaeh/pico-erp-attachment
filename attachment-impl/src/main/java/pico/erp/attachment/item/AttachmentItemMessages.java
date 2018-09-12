package pico.erp.attachment.item;

import java.io.InputStream;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.attachment.Attachment;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;

public interface AttachmentItemMessages {

  @Data
  class CreateRequest {

    @NotNull
    Attachment attachment;

    @NotNull
    String name;

    @NotNull
    String contentType;

    @NotNull
    @Min(1)
    Long contentLength;

    @NotNull
    InputStream inputStream;

    @NotNull
    AttachmentStorageStrategy attachmentStorageStrategy;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Data
  class DirectAccessRequest {

    @Valid
    @NotNull
    Auditor accessor;

  }

  @Value
  class DirectAccessResponse {

    Collection<Event> events;

  }

  @Data
  class UriAccessRequest {

    @Valid
    @NotNull
    Auditor accessor;
  }

  @Value
  class UriAccessResponse {

    Collection<Event> events;

  }

  @Data
  class RecoverRequest {


  }

  @Value
  class RecoverResponse {

    Collection<Event> events;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeleteRequest {

    boolean force;

  }

  @Value
  class DeleteResponse {

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
  class CopyRequest {

    AttachmentStorageStrategy attachmentStorageStrategy;

  }

  @Value
  class CopyResponse {

    AttachmentItem copied;

    Collection<Event> events;

  }

}
