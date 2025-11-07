package team.aliens.dms.domain.studyroom.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub

class StudyRoomAvailabilityTest : DescribeSpec({

    describe("checkIsAvailableGradeAndSex") {
        context("availableGrade가 0(전체)이고 availableSex가 ALL이면") {
            val studyRoom = createStudyRoomStub(
                availableGrade = 0,
                availableSex = Sex.ALL
            )

            it("어떤 학년, 성별이든 신청 가능하다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(1, Sex.MALE)
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.FEMALE)
                    studyRoom.checkIsAvailableGradeAndSex(3, Sex.MALE)
                }
            }
        }

        context("availableGrade가 2학년으로 제한되어 있을 때") {
            val studyRoom = createStudyRoomStub(
                availableGrade = 2,
                availableSex = Sex.ALL
            )

            it("2학년은 신청 가능하다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.MALE)
                }
            }

            it("1학년은 신청 불가능하다") {
                shouldThrow<StudyRoomAvailableGradeMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(1, Sex.MALE)
                }
            }

            it("3학년은 신청 불가능하다") {
                shouldThrow<StudyRoomAvailableGradeMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(3, Sex.MALE)
                }
            }
        }

        context("availableSex가 MALE로 제한되어 있을 때") {
            val studyRoom = createStudyRoomStub(
                availableGrade = 0,
                availableSex = Sex.MALE
            )

            it("남학생은 신청 가능하다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.MALE)
                }
            }

            it("여학생은 신청 불가능하다") {
                shouldThrow<StudyRoomAvailableSexMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.FEMALE)
                }
            }
        }

        context("2학년 여학생 전용 자습실일 때") {
            val studyRoom = createStudyRoomStub(
                availableGrade = 2,
                availableSex = Sex.FEMALE
            )

            it("2학년 여학생은 신청 가능하다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.FEMALE)
                }
            }

            it("2학년 남학생은 성별 불일치로 신청 불가능하다") {
                shouldThrow<StudyRoomAvailableSexMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(2, Sex.MALE)
                }
            }

            it("1학년 여학생은 학년 불일치로 신청 불가능하다") {
                shouldThrow<StudyRoomAvailableGradeMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(1, Sex.FEMALE)
                }
            }
        }
    }
})
