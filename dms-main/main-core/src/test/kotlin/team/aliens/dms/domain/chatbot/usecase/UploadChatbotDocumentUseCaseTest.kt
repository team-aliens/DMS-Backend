package team.aliens.dms.domain.chatbot.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentFileNameTooLongException
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentInvalidExtensionException
import team.aliens.dms.domain.chatbot.service.ChatbotService
import team.aliens.dms.domain.chatbot.stub.createChatbotDocumentStub
import java.time.LocalDateTime
import java.util.UUID

class UploadChatbotDocumentUseCaseTest : DescribeSpec({

    val chatbotService = mockk<ChatbotService>()
    val securityService = mockk<SecurityService>()
    val useCase = UploadChatbotDocumentUseCase(chatbotService, securityService)

    val schoolId = UUID.randomUUID()
    val content = "# 기숙사 생활 규정\n\n## 외박\n외박은 전날 18시까지 신청한다."
    val publishedAt = LocalDateTime.of(2026, 3, 1, 0, 0)

    describe("execute") {
        context("허용된 확장자의 파일을 올리면") {
            it("현재 학교로 문서를 인제스천하고 문서 id를 반환한다") {
                forAll(
                    row("dormitory-rules.md"),
                    row("notice.txt"),
                    row("RULES.MD")
                ) { fileName ->
                    val document = createChatbotDocumentStub(sourceUri = fileName, schoolId = schoolId)

                    every { securityService.getCurrentSchoolId() } returns schoolId
                    every {
                        chatbotService.ingestChatbotDocument(schoolId, fileName, content, publishedAt)
                    } returns document

                    val response = useCase.execute(fileName, content, publishedAt)

                    response.documentId shouldBe document.id
                    verify(exactly = 1) {
                        chatbotService.ingestChatbotDocument(schoolId, fileName, content, publishedAt)
                    }

                    clearAllMocks()
                }
            }
        }

        context("허용되지 않은 확장자의 파일을 올리면") {
            it("ChatbotDocumentInvalidExtensionException 이 발생하고 인제스천하지 않는다") {
                forAll(
                    row("rules.pdf"),
                    row("rules.hwp"),
                    row("rules"),
                    row("rules.md.exe")
                ) { fileName ->

                    shouldThrow<ChatbotDocumentInvalidExtensionException> {
                        useCase.execute(fileName, content, publishedAt)
                    }

                    clearAllMocks()
                }
            }
        }

        context("파일 이름이 500자를 넘으면") {
            it("ChatbotDocumentFileNameTooLongException 이 발생하고 인제스천하지 않는다") {
                val fileName = "a".repeat(498) + ".md"

                shouldThrow<ChatbotDocumentFileNameTooLongException> {
                    useCase.execute(fileName, content, publishedAt)
                }

                clearAllMocks()
            }
        }
    }
})
