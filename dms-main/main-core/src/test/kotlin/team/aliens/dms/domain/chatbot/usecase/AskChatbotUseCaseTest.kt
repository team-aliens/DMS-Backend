package team.aliens.dms.domain.chatbot.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.chatbot.exception.ChatbotAnswerGenerationFailedException
import team.aliens.dms.domain.chatbot.model.ChatbotAnswer
import team.aliens.dms.domain.chatbot.model.ChatbotQueryStatus
import team.aliens.dms.domain.chatbot.model.TokenUsage
import team.aliens.dms.domain.chatbot.service.ChatbotService
import java.util.UUID

class AskChatbotUseCaseTest : DescribeSpec({

    val chatbotService = mockk<ChatbotService>()
    val securityService = mockk<SecurityService>()
    val useCase = AskChatbotUseCase(chatbotService, securityService)

    val schoolId = UUID.randomUUID()

    describe("execute") {
        context("학생이 질문을 하면") {

            val question = "통금 시간이 몇 시야?"
            val answer = ChatbotAnswer(
                answer = "평일 통금 시간은 오후 10시입니다.",
                status = ChatbotQueryStatus.ANSWERED,
                retrievedChunkIds = listOf(UUID.randomUUID()),
                usage = TokenUsage(promptTokens = 100, candidatesTokens = 20, totalTokens = 120)
            )

            it("챗봇 답변을 반환하고 질의 로그를 남긴다") {

                every { securityService.getCurrentSchoolId() } returns schoolId
                every { chatbotService.generateAnswer(schoolId, question) } returns answer
                every { chatbotService.saveChatbotQueryLog(any()) } answers { firstArg() }

                val response = useCase.execute(question)

                response.answer shouldBe answer.answer
                verify(exactly = 1) { chatbotService.generateAnswer(schoolId, question) }
                verify(exactly = 1) {
                    chatbotService.saveChatbotQueryLog(
                        match { it.status == ChatbotQueryStatus.ANSWERED && it.answer == answer.answer }
                    )
                }
                clearAllMocks()
            }
        }

        context("답변 생성에 실패하면") {

            val question = "통금 시간이 몇 시야?"

            it("ChatbotAnswerGenerationFailedException 이 발생하고 FAILED 로그를 남긴다") {

                every { securityService.getCurrentSchoolId() } returns schoolId
                every {
                    chatbotService.generateAnswer(schoolId, question)
                } throws ChatbotAnswerGenerationFailedException
                every { chatbotService.saveChatbotQueryLog(any()) } answers { firstArg() }

                shouldThrow<ChatbotAnswerGenerationFailedException> {
                    useCase.execute(question)
                }
                verify(exactly = 1) {
                    chatbotService.saveChatbotQueryLog(match { it.status == ChatbotQueryStatus.FAILED })
                }
                clearAllMocks()
            }
        }
    }
})
