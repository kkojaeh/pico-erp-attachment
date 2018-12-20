package pico.erp.attachment.storage;

import java.io.InputStream;
import java.net.URI;
import pico.erp.attachment.item.AttachmentItemInfo;

public interface AttachmentStorageStrategy {

  AttachmentStorageKey copy(AttachmentStorageKey storageKey);

  boolean exists(AttachmentStorageKey storageKey);

  URI getUri(AttachmentStorageKey storageKey);

  boolean isUriSupported();

  InputStream load(AttachmentStorageKey storageKey);

  void remove(AttachmentStorageKey storageKey);

  AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream);

}
