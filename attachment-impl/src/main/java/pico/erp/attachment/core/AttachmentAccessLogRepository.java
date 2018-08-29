package pico.erp.attachment.core;

import pico.erp.attachment.domain.AttachmentAccessLog;

public interface AttachmentAccessLogRepository {

  AttachmentAccessLog create(AttachmentAccessLog log);

}
