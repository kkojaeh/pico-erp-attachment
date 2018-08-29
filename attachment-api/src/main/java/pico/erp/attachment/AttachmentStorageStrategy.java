package pico.erp.attachment;

import java.io.InputStream;
import java.net.URI;
import pico.erp.attachment.data.AttachmentItemInfo;
import pico.erp.attachment.data.AttachmentStorageKey;

public interface AttachmentStorageStrategy {

  AttachmentStorageKey copy(AttachmentStorageKey storageKey);

  boolean exists(AttachmentStorageKey storageKey);

  URI getUri(AttachmentStorageKey storageKey);

  boolean isUriSupported();

  InputStream load(AttachmentStorageKey storageKey);

  void remove(AttachmentStorageKey storageKey);

  AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream);

}
