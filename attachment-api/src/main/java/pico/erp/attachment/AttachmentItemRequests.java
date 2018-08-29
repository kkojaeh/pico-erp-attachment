package pico.erp.attachment;

import java.io.InputStream;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentItemId;

public interface AttachmentItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DirectAccessRequest {

    @Valid
    @NotNull
    AttachmentItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class UriAccessRequest {


    @Valid
    @NotNull
    AttachmentItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    AttachmentId attachmentId;

    @NotNull
    String name;

    @NotNull
    String contentType;

    @NotNull
    @Min(1)
    Long contentLength;

    @NotNull
    InputStream inputStream;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GetThumbnailRequest {

    @Valid
    @NotNull
    AttachmentItemId id;

    int width;

    int height;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class RecoverRequest {

    @Valid
    @NotNull
    AttachmentItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    AttachmentItemId id;

    boolean force;

  }

}
