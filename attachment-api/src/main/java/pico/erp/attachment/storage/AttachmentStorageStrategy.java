package pico.erp.attachment.storage;

import java.io.InputStream;
import java.net.URI;
import pico.erp.attachment.item.data.AttachmentItemInfo;
import pico.erp.attachment.storage.data.AttachmentStorageKey;

public interface AttachmentStorageStrategy {

  AttachmentStorageKey copy(AttachmentStorageKey storageKey);

  boolean exists(AttachmentStorageKey storageKey);

  URI getUri(AttachmentStorageKey storageKey);

  boolean isUriSupported();

  InputStream load(AttachmentStorageKey storageKey);

  void remove(AttachmentStorageKey storageKey);

  AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream);

}
