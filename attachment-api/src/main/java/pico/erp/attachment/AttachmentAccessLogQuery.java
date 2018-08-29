package pico.erp.attachment;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pico.erp.attachment.data.AttachmentAccessLogView;

public interface AttachmentAccessLogQuery {

  Page<AttachmentAccessLogView> retrieve(@NotNull AttachmentAccessLogView.Filter filter,
    @NotNull Pageable pageable);

}
