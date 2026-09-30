package team.aliens.dms.domain.chatbot.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.chatbot.stub.createChatbotAnswerStub
import java.time.LocalDateTime
import java.util.UUID

class ChatbotQueryLogTest : DescribeSpec({

    describe("of") {
        val schoolId = UUID.randomUUID()
        val createdAt = LocalDateTime.of(2026, 9, 30, 12, 0)

        context("답변이 있으면") {
            val chunkIds = listOf(UUID.randomUUID(), UUID.randomUUID())
            val usage = TokenUsage(promptTokens = 100, candidatesTokens = 20, totalTokens = 120)
            val answer = createChatbotAnswerStub(
                answer = "외박은 전날 18시까지 신청한다.",
                status = ChatbotQueryStatus.ANSWERED,
                retrievedChunkIds = chunkIds,
                usage = usage
            )

            val log = ChatbotQueryLog.of(schoolId, "외박 신청 기한은?", answer, 850L, createdAt)

            it("답변의 상태·내용·청크·토큰 사용량을 그대로 기록한다") {
                log.status shouldBe ChatbotQueryStatus.ANSWERED
                log.answer shouldBe "외박은 전날 18시까지 신청한다."
                log.retrievedChunkIds shouldBe chunkIds
                log.usage shouldBe usage
            }

            it("질문·응답 시간·학교·생성 시각을 기록한다") {
                log.question shouldBe "외박 신청 기한은?"
                log.responseTimeMs shouldBe 850L
                log.schoolId shouldBe schoolId
                log.createdAt shouldBe createdAt
            }
        }

        context("답변이 없으면") {
            val log = ChatbotQueryLog.of(schoolId, "외박 신청 기한은?", null, 3000L, createdAt)

            it("실패로 기록하고 답변·청크·토큰 사용량을 비운다") {
                log.status shouldBe ChatbotQueryStatus.FAILED
                log.answer shouldBe null
                log.retrievedChunkIds.shouldBeEmpty()
                log.usage shouldBe TokenUsage.EMPTY
            }
        }
    }
})
