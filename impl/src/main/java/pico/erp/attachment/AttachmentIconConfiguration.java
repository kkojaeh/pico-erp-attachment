package pico.erp.attachment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "icon")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentIconConfiguration {

  @Setter
  Resource unknown;

  @Setter
  Resource location;

  @Setter
  String extension;

  @Setter
  String contentType;

  @Getter
  @Setter
  List<String> names = new ArrayList<>();

  @Getter
  @Setter
  Map<String, String> synonyms = new HashMap<>();

  @SneakyThrows
  public AttachmentImageData get(String contentType) {
    InputStream inputStream = null;
    long contentLength = 0l;
    String iconKey = contentType.replaceAll("/", "-");
    if (names.contains(iconKey)) {
      if (synonyms.containsKey(iconKey)) {
        iconKey = synonyms.get(iconKey);
      }
      Resource icon = location.createRelative(iconKey + extension);
      if (icon != null && icon.exists()) {
        contentLength = icon.contentLength();
        inputStream = icon.getInputStream();
      }
    }
    if (inputStream == null) {
      contentLength = unknown.contentLength();
      inputStream = unknown.getInputStream();
    }
    return AttachmentImageData.builder()
      .contentType(this.contentType)
      .contentLength(contentLength)
      .inputStream(inputStream)
      .build();

  }

}
