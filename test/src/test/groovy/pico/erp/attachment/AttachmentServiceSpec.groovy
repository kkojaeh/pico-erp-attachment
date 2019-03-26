package pico.erp.attachment

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.attachment.access.log.AttachmentAccessLogQuery
import pico.erp.attachment.access.log.AttachmentAccessLogView
import pico.erp.attachment.category.AttachmentCategory
import pico.erp.attachment.category.AttachmentCategoryId
import pico.erp.attachment.item.AttachmentItemId
import pico.erp.attachment.item.AttachmentItemRequests
import pico.erp.attachment.item.AttachmentItemService
import pico.erp.attachment.storage.AttachmentStorageStrategy
import pico.erp.attachment.storage.FileSystemAttachmentStorageStrategy
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [AttachmentApplication])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [])
@ComponentScan(useDefaultFilters = false)
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
class AttachmentServiceSpec extends Specification {

  @Bean
  AttachmentCategory testAttachmentCategory() {
    return new AttachmentCategory.AttachmentCategoryImpl(
      AttachmentCategoryId.from("TEST"),
      "테스트"
    )
  }

  @Value('${attachment.storage.root-dir}')
  private File rootDir

  @Bean
  AttachmentStorageStrategy testFileSystemAttachmentItemStorage() {
    def config = FileSystemAttachmentStorageStrategy.Config.builder()
      .rootDirectory(rootDir)
      .build()
    return new FileSystemAttachmentStorageStrategy(config)
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

  def categoryId = AttachmentCategoryId.from("TEST")

  def multipleId = AttachmentId.from("multiple")

  def singularId = AttachmentId.from("singular")

  def multipleItemId = AttachmentItemId.from("multiple-item")

  def multipleItemId2 = AttachmentItemId.from("multiple-item2")

  def singularItemId = AttachmentItemId.from("singular-item")

  def singularItemId2 = AttachmentItemId.from("singular-item2")

  def setup() {
    attachmentService.create(
      new AttachmentRequests.CreateRequest(id: multipleId, multiple: true, categoryId: categoryId)
    )
    attachmentService.create(
      new AttachmentRequests.CreateRequest(id: singularId, multiple: false, categoryId: categoryId)
    )
  }

  def "단일 첨부에 두개의 파일을 추가하면 마지막 파일만 존재"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId2,
      attachmentId: singularId,
      name: "테스트 파일2.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    def items = attachmentItemService.getAll(singularId)

    then:
    items.size() == 1
  }


  def "복수 첨부에 파일을 추가하면 모두 존재"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: multipleItemId,
      attachmentId: multipleId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: multipleItemId2,
      attachmentId: multipleId,
      name: "테스트 파일2.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    def items = attachmentItemService.getAll(multipleId)

    then:
    items.size() == 2
    items[0].name == "테스트 파일.txt"
    items[1].name == "테스트 파일2.txt"
  }

  def "첨부에서 파일을 삭제(not force)해도 복구하면 접근이 가능하다"() {
    when:
    def item = attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.delete(
      new AttachmentItemRequests.DeleteRequest(
        id: singularItemId,
        force: false
      )
    )

    attachmentItemService.recover(
      new AttachmentItemRequests.RecoverRequest(
        id: singularItemId
      )
    )

    def accessedContent = IOUtils.toString(attachmentItemService.access(
      new AttachmentItemRequests.DirectAccessRequest(
        id: singularItemId
      )
    ), "UTF-8")

    then:
    accessedContent == testContent
  }

  def "첨부를 삭제(not force)해도 바로 지워지지 않고 접근 방지 된다"() {
    when:
    def item = attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.delete(
      new AttachmentRequests.DeleteRequest(
        id: singularId,
        force: false
      )
    )
    def accessedContent = IOUtils.toString(attachmentItemService.access(
      new AttachmentItemRequests.DirectAccessRequest(
        id: singularItemId
      )
    ), "UTF-8")

    then:
    thrown(AttachmentExceptions.CannotAccessException)
  }

  def "첨부를 삭제하고 현재시간기준으로 정리를 하면 접근할 수 없다"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentService.delete(
      new AttachmentRequests.DeleteRequest(
        id: singularId,
        force: false
      )
    )

    attachmentService.clear(
      new AttachmentRequests.ClearRequest(
        fixedDate: OffsetDateTime.now()
      )
    )

    attachmentService.get(singularId)

    then:
    thrown(AttachmentExceptions.NotFoundException)
  }

  def "첨부에 URI 접근하면 로그가 기록 된다"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.access(
      new AttachmentItemRequests.UriAccessRequest(
        id: singularItemId
      )
    )

    def logs = attachmentAccessLogQuery.retrieve(
      new AttachmentAccessLogView.Filter(),
      new PageRequest(0, 10)
    )

    then:
    logs.totalElements == 1
    logs.content.get(0).accessType == AttachmentAccessTypeKind.URI
  }

  def "첨부에 DIRECT 접근하면 로그가 기록 된다"() {
    when:
    attachmentItemService.create(new AttachmentItemRequests.CreateRequest(
      id: singularItemId,
      attachmentId: singularId,
      name: "테스트 파일.txt",
      contentType: "text/plain",
      contentLength: testContent.getBytes().length,
      inputStream: new ByteArrayInputStream(testContent.getBytes())
    ))

    attachmentItemService.access(
      new AttachmentItemRequests.DirectAccessRequest(
        id: singularItemId
      )
    )

    def logs = attachmentAccessLogQuery.retrieve(
      new AttachmentAccessLogView.Filter(),
      new PageRequest(0, 10)
    )

    then:
    logs.totalElements == 1
    logs.content.get(0).accessType == AttachmentAccessTypeKind.DIRECT
  }

}
