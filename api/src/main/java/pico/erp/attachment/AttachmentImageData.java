package pico.erp.attachment;

import java.io.InputStream;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttachmentImageData {

  String contentType;

  long contentLength;

  InputStream inputStream;

}
