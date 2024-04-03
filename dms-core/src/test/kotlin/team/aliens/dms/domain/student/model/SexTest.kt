package team.aliens.dms.domain.student.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.student.exception.SexMismatchException

class SexTest : DescribeSpec({

    describe("transferToSex") {
        context("성별이 남자면") {
            val sex = Sex.transferToSex("남")

            it("MALE을 반환한다") {
                sex shouldBe Sex.MALE
            }
        }

        context("성별이 여자면") {
            val sex = Sex.transferToSex("여")

            it("FEMALE을 반환한다") {
                sex shouldBe Sex.FEMALE
            }
        }

        context("성별 타입이 전체이면") {
            it("에러가 발생한다") {
                shouldThrow<SexMismatchException> {
                    Sex.transferToSex("전체")
                }
            }
        }
    }
})
