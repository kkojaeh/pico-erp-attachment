package pico.erp.attachment.item;

public interface AttachmentItemInfo {

  long getContentLength();

  String getContentType();

  AttachmentItemId getId();

  String getName();

}
