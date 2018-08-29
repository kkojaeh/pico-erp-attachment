package pico.erp.attachment.impl.jpa;

import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.attachment.data.AttachmentAccessTypeKind;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.attachment.data.AttachmentItemId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Getter
@Entity(name = "AttachmentAccessLog")
@Table(name = "ATM_ATTACHMENT_ACCESS_LOG", indexes = {
  @Index(name = "ATM_ATTACHMENT_ACCESS_LOG_ATTACHMENT_ID_IDX", columnList = "ATTACHMENT_ID")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentAccessLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false, nullable = false)
  Long id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ATTACHMENT_ID", length = TypeDefinitions.ID_LENGTH, updatable = false))
  })
  AttachmentId attachmentId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ATTACHMENT_ITEM_ID", length = TypeDefinitions.ID_LENGTH, updatable = false))
  })
  AttachmentItemId attachmentItemId;

  @Column(length = TypeDefinitions.FILE_NAME_LENGTH, updatable = false)
  String name;

  @Column(length = TypeDefinitions.CONTENT_TYPE_LENGTH, updatable = false)
  String contentType;

  @Column(updatable = false)
  long contentLength;

  @Enumerated(EnumType.STRING)
  @Column(length = TypeDefinitions.ENUM_LENGTH, updatable = false)
  AttachmentAccessTypeKind accessType;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "ACCESSOR_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "ACCESSOR_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor accessor;


  @Column(updatable = false)
  OffsetDateTime accessedDate;

}
