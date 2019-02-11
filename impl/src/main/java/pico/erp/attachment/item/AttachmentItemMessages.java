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

  interface Create {

    @Data
    class Request {

      AttachmentItemId id;

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
    class Response {

      Collection<Event> events;

    }

  }

  interface DirectAccess {

    @Data
    class Request {

      @Valid
      @NotNull
      Auditor accessor;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface UriAccess {

    @Data
    class Request {

      @Valid
      @NotNull
      Auditor accessor;
    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Recover {

    @Data
    class Request {


    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Delete {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {

      boolean force;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Clear {

    @Data
    class Request {


    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Copy {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {

      AttachmentStorageStrategy attachmentStorageStrategy;

      @NotNull
      Attachment toAttachment;

    }

    @Value
    class Response {

      AttachmentItem copied;

      Collection<Event> events;

    }

  }















}
