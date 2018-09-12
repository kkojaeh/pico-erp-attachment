package pico.erp.attachment.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AttachmentItemExceptions {


  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "attachment.item.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "attachment.item.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "attachment.item.cannot.delete.exception")
  class CannotDeleteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "attachment.item.cannot.recover.exception")
  class CannotRecoverException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }


}
