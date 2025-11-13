package team.aliens.dms.domain.school.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import java.util.UUID

class CheckSchoolAnswerUseCaseTest : DescribeSpec({

    describe("execute") {
        val schoolService = mockk<SchoolService>()
        val checkSchoolAnswerUseCase = CheckSchoolAnswerUseCase(schoolService)

        val schoolId = UUID.randomUUID()
        val correctAnswer = "정답"
        val wrongAnswer = "오답"

        val school = School(
            id = schoolId,
            name = "테스트 고등학교",
            code = "TEST123",
            question = "우리 학교는?",
            answer = correctAnswer,
            address = "서울시 강남구",
            contractStartedAt = java.time.LocalDate.now(),
            contractEndedAt = null
        )

        context("올바른 답변이 주어지면") {
            every { schoolService.getSchoolById(schoolId) } returns school

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    checkSchoolAnswerUseCase.execute(schoolId, correctAnswer)
                }
            }
        }

        context("잘못된 답변이 주어지면") {
            every { schoolService.getSchoolById(schoolId) } returns school

            it("AnswerMismatchException이 발생한다") {
                shouldThrow<AnswerMismatchException> {
                    checkSchoolAnswerUseCase.execute(schoolId, wrongAnswer)
                }
            }
        }
    }
})
