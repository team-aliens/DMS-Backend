package team.aliens.dms.domain.student.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.student.stub.createStudentStub
import java.util.UUID

class StudentTest : DescribeSpec({

    describe("processGcn") {
        context("학년, 반, 번호를 받으면") {
            val gcn = Student.processGcn(3, 2, 1)

            it("학번 4자리를 반환한다") {
                gcn shouldBe "3201"
            }
        }

        context("번호가 한 자리면") {
            val number = 9
            val gcn = Student.processGcn(3, 2, number)

            it("앞에 0을 붙여 두 자리로 만든다") {
                gcn shouldBe "3209"
            }
        }

        context("번호가 두 자리면") {
            val number = 10
            val gcn = Student.processGcn(3, 2, number)

            it("그대로 붙인다") {
                gcn shouldBe "3210"
            }
        }
    }

    describe("parseGcn") {
        context("학번을 String으로 넣으면") {
            val gcn = Student.parseGcn("3201")

            it("학년, 반, 번호를 반환한다") {
                gcn shouldBe Triple(3, 2, 1)
            }
        }

        context("번호가 두 자리인 학번을 넣으면") {
            val gcn = Student.parseGcn("3215")

            it("번호를 두 자리 그대로 반환한다") {
                gcn shouldBe Triple(3, 2, 15)
            }
        }
    }

    describe("gcn") {
        context("학생을 만들면") {
            val student = createStudentStub(grade = 1, classRoom = 4, number = 12)

            it("학년, 반, 번호로 학번이 정해진다") {
                student.gcn shouldBe "1412"
            }
        }
    }

    describe("hasUser") {
        context("가입한 계정이 연결돼 있으면") {
            val student = createStudentStub(userId = UUID.randomUUID())

            it("true를 반환한다") {
                student.hasUser shouldBe true
            }
        }

        context("가입한 계정이 없으면") {
            val student = createStudentStub(userId = null)

            it("false를 반환한다") {
                student.hasUser shouldBe false
            }
        }
    }
})
