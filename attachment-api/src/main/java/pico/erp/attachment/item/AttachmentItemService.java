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

  InputStream access(@Valid AttachmentItemRequests.DirectAccessRequest request);

  URI access(@Valid AttachmentItemRequests.UriAccessRequest request);

  AttachmentItemData create(@Valid AttachmentItemRequests.CreateRequest request);

  void delete(@Valid AttachmentItemRequests.DeleteRequest request);

  AttachmentItemData get(AttachmentItemId id);

  List<AttachmentItemData> getAll(AttachmentId attachmentId);

  AttachmentImageData getThumbnail(@NotNull AttachmentItemRequests.GetThumbnailRequest request);

  void recover(@Valid RecoverRequest request);

}
