package pico.erp.attachment.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import pico.erp.attachment.item.data.AttachmentItemInfo;
import pico.erp.attachment.storage.AttachmentStorageStrategy;
import pico.erp.attachment.storage.data.AttachmentStorageKey;

public class FileSystemAttachmentStorageStrategy implements AttachmentStorageStrategy {

  @Setter
  @Value("${attachment.storage.root-dir}")
  private File rootDir;

  @SneakyThrows
  @Override
  public AttachmentStorageKey copy(AttachmentStorageKey storageKey) {
    File file = new File(rootDir, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    }
    File copied = new File(file.getParentFile(), UUID.randomUUID().toString());
    FileUtils.copyFile(file, copied);
    return AttachmentStorageKey.from(rootDir.toURI().relativize(copied.toURI()).toString());
  }

  @Override
  public boolean exists(AttachmentStorageKey storageKey) {
    return new File(rootDir, storageKey.getValue()).exists();
  }

  @Override
  public URI getUri(AttachmentStorageKey storageKey) {
    return null;
  }

  @Override
  public boolean isUriSupported() {
    return false;
  }

  @Override
  @SneakyThrows
  public InputStream load(AttachmentStorageKey storageKey) {
    File file = new File(rootDir, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      return new FileInputStream(file);
    }
  }

  @Override
  public void remove(AttachmentStorageKey storageKey) {
    File file = new File(rootDir, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      file.delete();
    }
  }

  @Override
  @SneakyThrows
  public AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream) {
    File destFile = new File(rootDir, info.getId().getValue());
    FileUtils.copyInputStreamToFile(inputStream, destFile);
    return AttachmentStorageKey.from(rootDir.toURI().relativize(destFile.toURI()).toString());
  }
}
