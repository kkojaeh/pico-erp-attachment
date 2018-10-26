package pico.erp.attachment.item;

import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.attachment.AttachmentId;
import pico.erp.attachment.storage.AttachmentStorageKey;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Data
@Entity(name = "AttachmentItem")
@Table(name = "ATM_ATTACHMENT_ITEM", indexes = {
  @Index(columnList = "ATTACHMENT_ID")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentItemEntity {

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  AttachmentItemId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ATTACHMENT_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  AttachmentId attachmentId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "STORAGE_KEY", length = TypeDefinitions.ID_LENGTH))
  })
  AttachmentStorageKey storageKey;

  @Column(length = TypeDefinitions.FILE_NAME_LENGTH)
  String name;

  @Column(length = TypeDefinitions.CONTENT_TYPE_LENGTH)
  String contentType;

  @Column
  long contentLength;

  @Column
  boolean deleted;

  OffsetDateTime deletedDate;

  OffsetDateTime lastAccessedDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime createdDate;

}
