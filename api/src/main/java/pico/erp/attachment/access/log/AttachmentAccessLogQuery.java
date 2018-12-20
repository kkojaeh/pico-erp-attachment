package pico.erp.attachment.access.log;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttachmentAccessLogQuery {

  Page<AttachmentAccessLogView> retrieve(@NotNull AttachmentAccessLogView.Filter filter,
    @NotNull Pageable pageable);

}
