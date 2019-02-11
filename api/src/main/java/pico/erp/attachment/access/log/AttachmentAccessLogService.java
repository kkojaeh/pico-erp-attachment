package pico.erp.attachment.access.log;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface AttachmentAccessLogService {

  void create(@Valid @NotNull AttachmentAccessLogRequests.CreateRequest request);
}
