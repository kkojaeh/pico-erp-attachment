package pico.erp.attachment.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import pico.erp.attachment.item.AttachmentItemInfo;

public class FileSystemAttachmentStorageStrategy implements AttachmentStorageStrategy {

  private final File rootDirectory;

  public FileSystemAttachmentStorageStrategy(Config config) {
    rootDirectory = config.rootDirectory;
  }

  @SneakyThrows
  @Override
  public AttachmentStorageKey copy(AttachmentStorageKey storageKey) {
    File file = new File(rootDirectory, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    }
    File copied = new File(file.getParentFile(), UUID.randomUUID().toString());
    FileUtils.copyFile(file, copied);
    return AttachmentStorageKey.from(rootDirectory.toURI().relativize(copied.toURI()).toString());
  }

  @Override
  public boolean exists(AttachmentStorageKey storageKey) {
    return new File(rootDirectory, storageKey.getValue()).exists();
  }

  @Override
  @SneakyThrows
  public InputStream load(AttachmentStorageKey storageKey) {
    File file = new File(rootDirectory, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      return new FileInputStream(file);
    }
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
  public void remove(AttachmentStorageKey storageKey) {
    File file = new File(rootDirectory, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      file.delete();
    }
  }

  @Override
  @SneakyThrows
  public AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream) {
    File destFile = new File(rootDirectory, info.getId().getValue().toString());
    FileUtils.copyInputStreamToFile(inputStream, destFile);
    return AttachmentStorageKey.from(rootDirectory.toURI().relativize(destFile.toURI()).toString());
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    private File rootDirectory;

  }
}
