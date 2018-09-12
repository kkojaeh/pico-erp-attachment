package pico.erp.attachment.item;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentImageData;
import pico.erp.attachment.item.AttachmentItemRequests.RecoverRequest;
import pico.erp.attachment.item.data.AttachmentItemData;
import pico.erp.attachment.item.data.AttachmentItemId;

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
