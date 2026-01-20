package team.aliens.dms.domain.school.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.stub.createSchoolStub

class SchoolTest : DescribeSpec({

    describe("checkAnswer") {
        context("정답이 일치하면") {
            val school = createSchoolStub(answer = "correct answer")

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    school.checkAnswer("correct answer")
                }
            }
        }

        context("정답이 일치하지 않으면") {
            val school = createSchoolStub(answer = "correct answer")

            it("AnswerMismatchException을 발생시킨다") {
                shouldThrow<AnswerMismatchException> {
                    school.checkAnswer("wrong answer")
                }
            }
        }
    }
})
