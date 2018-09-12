package pico.erp.attachment

import lombok.Getter
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.attachment.access.log.AttachmentAccessLogQuery
import pico.erp.attachment.category.data.AttachmentCategory
import pico.erp.attachment.category.data.AttachmentCategoryId
import pico.erp.attachment.data.AttachmentData
import pico.erp.attachment.impl.FileSystemAttachmentStorageStrategy
import pico.erp.attachment.item.AttachmentItemRequests
import pico.erp.attachment.item.AttachmentItemService
import pico.erp.attachment.storage.AttachmentStorageStrategy
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.Public
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
class AttachmentServiceSpec extends Specification {

  @Getter
  class TestAttachmentCategory implements AttachmentCategory {

    AttachmentCategoryId id = AttachmentCategoryId.from("TEST")

    String name = "테스트"
  }

  @Public
  @Bean
  AttachmentCategory testAttachmentCategory() {
    return new TestAttachmentCategory()
  }

  @Public
  @Bean
  AttachmentStorageStrategy testFileSystemAttachmentItemStorage() {
    return new FileSystemAttachmentStorageStrategy()
  }

  @Autowired
  AttachmentService attachmentService

  @Autowired
  AttachmentItemService attachmentItemService

  AttachmentData multiple

  AttachmentData singular

  @Autowired
  AttachmentAccessLogQuery attachmentAccessLogQuery

  @Autowired
  AttachmentStorageStrategy attachmentStorageStrategy

  def testContent = """
헬로 그냥 텍스트
"""

  def setup() {
    multiple = attachmentService.create(new AttachmentRequests.CreateRequest(multiple: true, categoryId: AttachmentCategoryId.from("TEST")))
    singular = attachmentService.create(new AttachmentRequests.CreateRequest(multiple: false, categoryId: AttachmentCategoryId.from("TEST")))
  }

  def "단일 첨부에 두개의 파일을 추가하면 마지막 파일만 존재"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: singular.id,
      name: "테스트 파일2.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    def items = attachmentItemService.getAll(singular.id)

    then:
    items.size() == 2
    items.stream().filter({ item -> item.deleted }).count() == 1
  }


  def "복수 첨부에 파일을 추가하면 모두 존재"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: multiple.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: multiple.id,
      name: "테스트 파일2.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    def items = attachmentItemService.getAll(multiple.id)

    then:
    items.size() == 2
    items.get(0).name == "테스트 파일.txt"
    items.get(1).name == "테스트 파일2.txt"
  }

  def "첨부에서 파일을 삭제(not force)해도 복구하면 접근이 가능하다"() {
    when:
    def item = attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.delete(
      new AttachmentItemRequests.DeleteRequest(
        id: item.id,
        force: false
      )
    )

    attachmentItemService.recover(
      new AttachmentItemRequests.RecoverRequest(
        id: item.id
      )
    )

    def accessedContent = IOUtils.toString(attachmentItemService.access(
      new AttachmentItemRequests.DirectAccessRequest(
        id: item.id
      )
    ), "UTF-8")

    then:
    accessedContent == testContent
  }

  def "첨부를 삭제(not force)해도 바로 지워지지 않고 접근 방지 된다"() {
    when:
    def item = attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.delete(
      new AttachmentRequests.DeleteRequest(
        id: singular.id,
        force: false
      )
    )
    def accessedContent = IOUtils.toString(attachmentItemService.access(
      new AttachmentItemRequests.DirectAccessRequest(
        id: item.id
      )
    ), "UTF-8")

    then:
      thrown(AttachmentExceptions.CannotAccessException)
  }

  def "첨부를 삭제하고 현재시간기준으로 정리를 하면 접근할 수 없다"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      attachmentId: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.delete(
      new AttachmentRequests.DeleteRequest(
        id: singular.id,
        force: false
      )
    )

    attachmentService.clear(
      new AttachmentRequests.ClearRequest(
        fixedDate: OffsetDateTime.now()
      )
    )

    attachmentService.get(singular.id)

    then:
    thrown(AttachmentExceptions.NotFoundException)
  }

  /*def "첨부에 URI 접근하면 로그가 기록 된다"() {
    when:
    def item = attachmentService.addItem(new AttachmentService.AddItemRequest(
      id: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.access(
      new AttachmentService.UriAccessRequest(
        id: singular.id,
        itemId: item.id
      )
    )

    def logs = attachmentAccessLogQuery.retrieve(
      new DefaultQueryFilter(),
      new PageRequest(0, 10)
    )

    then:
    logs.totalElements == 1
    logs.content.get(0).accessType == AttachmentAccessTypeKind.URI
  }

  def "첨부에 DIRECT 접근하면 로그가 기록 된다"() {
    when:
    def item = attachmentService.addItem(new AttachmentService.AddItemRequest(
      id: singular.id,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.access(
      new AttachmentService.DirectAccessRequest(
        id: singular.id,
        itemId: item.id
      )
    )

    def logs = attachmentAccessLogQuery.retrieve(
      new DefaultQueryFilter(),
      new PageRequest(0, 10)
    )

    then:
    logs.totalElements == 1
    logs.content.get(0).accessType == AttachmentAccessTypeKind.DIRECT
  }
*/

}
