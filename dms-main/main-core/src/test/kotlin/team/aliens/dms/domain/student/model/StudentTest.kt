package team.aliens.dms.domain.student.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StudentTest : DescribeSpec({

    describe("processGcn") {
        context("학년, 반, 번호를 받으면") {
            val gcn = Student.processGcn(3, 2, 1)

            it("학번 4자리를 반환한다") {
                gcn shouldBe "3201"
            }
        }
    }
})
