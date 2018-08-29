package pico.erp.attachment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.attachment.AttachmentRequests.ClearRequest;
import pico.erp.attachment.AttachmentRequests.CopyRequest;
import pico.erp.attachment.AttachmentRequests.CreateRequest;
import pico.erp.attachment.AttachmentRequests.DeleteRequest;
import pico.erp.attachment.data.AttachmentData;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentImageData;

public interface AttachmentService {
/*

  InputStream access(@Valid DirectAccessRequest request);

  URI access(@Valid UriAccessRequest request);

  AttachmentItemData addItem(@Valid AddItemRequest request);
*/

  void clear(@Valid ClearRequest request);

  AttachmentData copy(@Valid CopyRequest request);

  AttachmentData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  AttachmentData get(@NotNull AttachmentId id);

  AttachmentImageData getIcon(@NotNull String contentType);

  /*AttachmentImageData getThumbnail(@NotNull GetThumbnailRequest request);*/

  boolean isUriSupported();
/*
  void recoveryItem(@Valid RecoveryItemRequest request);

  void removeItem(@Valid RemoveItemRequest request);*/


}
