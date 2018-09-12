package pico.erp.attachment.item.data;

public interface AttachmentItemInfo {

  long getContentLength();

  String getContentType();

  AttachmentItemId getId();

  String getName();

}
