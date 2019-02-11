package pico.erp.attachment.access.log;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import pico.erp.attachment.AttachmentAccessTypeKind;
import pico.erp.attachment.item.AttachmentItemId;
import pico.erp.shared.data.Auditor;

public interface AttachmentAccessLogRequests {

  @Getter
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    AttachmentItemId itemId;

    @NotNull
    Auditor accessor;

    @NotNull
    AttachmentAccessTypeKind accessType;

  }
}
