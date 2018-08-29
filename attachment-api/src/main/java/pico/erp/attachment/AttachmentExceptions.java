package pico.erp.attachment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AttachmentExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "attachment.cannot.modify.exception")
  class CannotModifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "attachment.category.not.found.exception")
  class CategoryNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "attachment.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "attachment.cannot.access.exception")
  class CannotAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }
}
