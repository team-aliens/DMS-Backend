package team.aliens.dms.domain.volunteer.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.volunteer.exception.VolunteerNotAvailableException
import team.aliens.dms.domain.volunteer.stub.createVolunteerStub

class VolunteerTest : DescribeSpec({

    describe("isAvailable") {
        context("학생의 학년과 성별이 모두 일치하면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.MALE
            )
            val student = createStudentStub(
                grade = 2,
                sex = Sex.MALE
            )

            val result = volunteer.isAvailable(student)

            it("true를 반환한다") {
                result shouldBe true
            }
        }

        context("학년은 일치하고 성별이 ALL이면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.ALL
            )
            val student = createStudentStub(
                grade = 2,
                sex = Sex.FEMALE
            )

            val result = volunteer.isAvailable(student)

            it("true를 반환한다") {
                result shouldBe true
            }
        }

        context("학생의 학년이 일치하지 않으면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.MALE
            )
            val student = createStudentStub(
                grade = 1,
                sex = Sex.MALE
            )

            val result = volunteer.isAvailable(student)

            it("false를 반환한다") {
                result shouldBe false
            }
        }

        context("학생의 성별이 일치하지 않으면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.MALE
            )
            val student = createStudentStub(
                grade = 2,
                sex = Sex.FEMALE
            )

            val result = volunteer.isAvailable(student)

            it("false를 반환한다") {
                result shouldBe false
            }
        }
    }

    describe("checkAvailable") {
        context("신청 가능한 학생이면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.MALE
            )
            val student = createStudentStub(
                grade = 2,
                sex = Sex.MALE
            )

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    volunteer.checkAvailable(student)
                }
            }
        }

        context("신청 불가능한 학생이면") {
            val volunteer = createVolunteerStub(
                availableGrade = AvailableGrade.SECOND,
                availableSex = Sex.MALE
            )
            val student = createStudentStub(
                grade = 1,
                sex = Sex.FEMALE
            )

            it("VolunteerNotAvailableException을 발생시킨다") {
                shouldThrow<VolunteerNotAvailableException> {
                    volunteer.checkAvailable(student)
                }
            }
        }
    }
})
