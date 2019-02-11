package pico.erp.attachment.item;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.AttachmentImageData;
import pico.erp.attachment.item.AttachmentItemRequests.RecoverRequest;

public interface AttachmentItemService {

  InputStream access(@NotNull @Valid AttachmentItemRequests.DirectAccessRequest request);

  URI access(@NotNull @Valid AttachmentItemRequests.UriAccessRequest request);

  AttachmentItemData copy(@NotNull @Valid AttachmentItemRequests.CopyRequest request);

  AttachmentItemData create(@NotNull @Valid AttachmentItemRequests.CreateRequest request);

  void delete(@NotNull @Valid AttachmentItemRequests.DeleteRequest request);

  AttachmentItemData get(@NotNull @Valid AttachmentItemId id);

  List<AttachmentItemData> getAll(@NotNull @Valid AttachmentId attachmentId);

  AttachmentImageData getThumbnail(
    @NotNull @Valid AttachmentItemRequests.GetThumbnailRequest request);

  void recover(@NotNull @Valid RecoverRequest request);

}
