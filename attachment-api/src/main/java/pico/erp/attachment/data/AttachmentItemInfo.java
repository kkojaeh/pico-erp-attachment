package pico.erp.attachment.data;

public interface AttachmentItemInfo {

  long getContentLength();

  String getContentType();

  AttachmentItemId getId();

  String getName();

}
