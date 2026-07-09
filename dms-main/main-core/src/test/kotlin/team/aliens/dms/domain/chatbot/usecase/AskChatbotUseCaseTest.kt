package team.aliens.dms.domain.chatbot.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.domain.chatbot.exception.ChatbotAnswerGenerationFailedException
import team.aliens.dms.domain.chatbot.service.ChatbotService

class AskChatbotUseCaseTest : DescribeSpec({

    val chatbotService = mockk<ChatbotService>()

    val useCase = AskChatbotUseCase(chatbotService)

    describe("execute") {
        context("학생이 질문을 하면") {

            val question = "통금 시간이 몇 시야?"
            val answer = "평일 통금 시간은 오후 10시입니다."

            it("챗봇 답변을 반환한다") {
                every { chatbotService.generateAnswer(question) } returns answer

                val response = useCase.execute(question)

                response.answer shouldBe answer
                verify(exactly = 1) { chatbotService.generateAnswer(question) }
            }
        }

        context("답변 생성에 실패하면") {

            val question = "통금 시간이 몇 시야?"

            it("ChatbotAnswerGenerationFailedException 이 발생한다") {
                every {
                    chatbotService.generateAnswer(question)
                } throws ChatbotAnswerGenerationFailedException

                shouldThrow<ChatbotAnswerGenerationFailedException> {
                    useCase.execute(question)
                }
            }
        }
    }
})
