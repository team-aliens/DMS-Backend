package team.aliens.dms.domain.chatbot.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentNotFoundException
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentSchoolMismatchException
import team.aliens.dms.domain.chatbot.service.ChatbotService
import team.aliens.dms.domain.chatbot.stub.createChatbotDocumentStub
import java.util.UUID

class RemoveChatbotDocumentUseCaseTest : DescribeSpec({

    val schoolId = UUID.randomUUID()

    describe("execute") {
        context("현재 학교의 문서를 삭제하면") {
            it("그 문서를 삭제한다") {
                val chatbotService = mockk<ChatbotService>()
                val securityService = mockk<SecurityService>()
                val useCase = RemoveChatbotDocumentUseCase(chatbotService, securityService)
                val document = createChatbotDocumentStub(schoolId = schoolId)

                every { chatbotService.getChatbotDocumentById(document.id) } returns document
                every { securityService.getCurrentSchoolId() } returns schoolId
                every { chatbotService.deleteChatbotDocument(document) } just runs

                useCase.execute(document.id)

                verify(exactly = 1) { chatbotService.getChatbotDocumentById(document.id) }
                verify(exactly = 1) { chatbotService.deleteChatbotDocument(document) }
            }
        }

        context("다른 학교의 문서를 삭제하면") {
            it("ChatbotDocumentSchoolMismatchException 이 발생하고 삭제하지 않는다") {
                val chatbotService = mockk<ChatbotService>()
                val securityService = mockk<SecurityService>()
                val useCase = RemoveChatbotDocumentUseCase(chatbotService, securityService)
                val document = createChatbotDocumentStub(schoolId = UUID.randomUUID())

                every { chatbotService.getChatbotDocumentById(document.id) } returns document
                every { securityService.getCurrentSchoolId() } returns schoolId

                shouldThrow<ChatbotDocumentSchoolMismatchException> {
                    useCase.execute(document.id)
                }
                verify(exactly = 1) { chatbotService.getChatbotDocumentById(document.id) }
            }
        }

        context("문서가 없으면") {
            it("ChatbotDocumentNotFoundException 이 발생하고 삭제하지 않는다") {
                val chatbotService = mockk<ChatbotService>()
                val securityService = mockk<SecurityService>()
                val useCase = RemoveChatbotDocumentUseCase(chatbotService, securityService)
                val documentId = UUID.randomUUID()

                every { chatbotService.getChatbotDocumentById(documentId) } throws ChatbotDocumentNotFoundException

                shouldThrow<ChatbotDocumentNotFoundException> {
                    useCase.execute(documentId)
                }
                verify(exactly = 1) { chatbotService.getChatbotDocumentById(documentId) }
            }
        }
    }
})
