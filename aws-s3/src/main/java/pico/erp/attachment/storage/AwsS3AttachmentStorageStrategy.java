package pico.erp.attachment.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pico.erp.attachment.item.AttachmentItemInfo;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AwsS3AttachmentStorageStrategy implements AttachmentStorageStrategy {

  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  private final AmazonS3 amazonS3;

  private final String amazonS3BucketName;

  public AwsS3AttachmentStorageStrategy(Config config) {
    amazonS3 = config.getAmazonS3();
    amazonS3BucketName = config.getAmazonS3BucketName();
  }

  @Override
  public AttachmentStorageKey copy(AttachmentStorageKey storageKey) {
    AttachmentStorageKey copiedKey = createStorageKey(storageKey);
    amazonS3.copyObject(
      new CopyObjectRequest(amazonS3BucketName, storageKey.getValue(),
        amazonS3BucketName, copiedKey.getValue())
    );
    return copiedKey;
  }

  private AttachmentStorageKey createStorageKey(AttachmentStorageKey storageKey) {
    String key = String
      .format("%s/%s", formatter.format(LocalDateTime.now()), UUID.randomUUID());
    return AttachmentStorageKey.from(key);
  }

  private AttachmentStorageKey createStorageKey(AttachmentItemInfo info) {
    String key = String
      .format("%s/%s", formatter.format(LocalDateTime.now()), info.getId().getValue());
    return AttachmentStorageKey.from(key);
  }

  @Override
  public boolean exists(AttachmentStorageKey storageKey) {
    return amazonS3.doesObjectExist(amazonS3BucketName, storageKey.getValue());
  }

  @SneakyThrows
  @Override
  public URI getUri(AttachmentStorageKey storageKey) {
    return amazonS3.generatePresignedUrl(
      amazonS3BucketName,
      storageKey.getValue(),
      new Date(OffsetDateTime.now().plusMinutes(10).toInstant().toEpochMilli())
    ).toURI();
  }

  @Override
  public boolean isUriSupported() {
    return true;
  }

  @Override
  public InputStream load(AttachmentStorageKey storageKey) {
    S3Object object = amazonS3.getObject(
      new GetObjectRequest(amazonS3BucketName, storageKey.getValue())
    );
    return object.getObjectContent();
  }

  @Override
  public void remove(AttachmentStorageKey storageKey) {
    amazonS3.deleteObject(
      new DeleteObjectRequest(amazonS3BucketName, storageKey.getValue())
    );
  }

  @SneakyThrows
  @Override
  public AttachmentStorageKey save(AttachmentItemInfo info, InputStream inputStream) {
    AttachmentStorageKey storageKey = createStorageKey(info);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(info.getContentLength());
    metadata.setContentType(info.getContentType());
    metadata.setContentDisposition(
      String.format(
        "attachment; filename=\"%s\";",
        URLEncoder.encode(info.getName(), "UTF-8").replaceAll("\\+", " ")
      )
    );

    amazonS3.putObject(
      new PutObjectRequest(
        amazonS3BucketName,
        storageKey.getValue(),
        inputStream,
        metadata
      )
    );
    return storageKey;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    AmazonS3 amazonS3;

    String amazonS3BucketName;

  }


}
