package pico.erp.attachment;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.attachment.category.data.AttachmentCategoryId;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.item.data.AttachmentItemId;

public interface AttachmentRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class ClearRequest {

    /**
     * 지정 기준시간보다 예전 데이터를 삭제
     */
    @NotNull
    @Past
    OffsetDateTime fixedDate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CreateRequest {

    boolean multiple;

    @Valid
    @NotNull
    AttachmentCategoryId categoryId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CopyRequest {

    @Valid
    @NotNull
    AttachmentId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    AttachmentId id;

    boolean force;

  }

  /*@Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class AddItemRequest {

    @Valid
    @NotNull
    AttachmentId id;

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
*/


  /*@Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class RemoveItemRequest {

    @Valid
    @NotNull
    AttachmentId id;

    @Valid
    @NotNull
    AttachmentItemId itemId;

    boolean force;

  }*/

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class RecoveryItemRequest {

    @Valid
    @NotNull
    AttachmentId id;

    @Valid
    @NotNull
    AttachmentItemId itemId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GetThumbnailRequest {

    @Valid
    @NotNull
    AttachmentId id;

    @Valid
    @NotNull
    AttachmentItemId itemId;

    int width;

    int height;

  }

}
