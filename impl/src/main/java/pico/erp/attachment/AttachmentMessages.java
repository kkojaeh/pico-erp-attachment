package pico.erp.attachment;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.attachment.category.AttachmentCategory;
import pico.erp.shared.event.Event;

public interface AttachmentMessages {


  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      AttachmentId id;

      @Valid
      @NotNull
      AttachmentCategory category;

      boolean multiple;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Delete {

    @Data
    class Request {

      boolean force;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Copy {

    @Data
    class Request {

    }

    @Value
    class Response {

      Attachment copied;

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

  interface Access {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }






}
