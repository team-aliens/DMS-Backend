package team.aliens.dms.domain.studyroom.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub

class StudyRoomTest : DescribeSpec({

    describe("checkIsAvailableGradeAndSex") {
        context("현재 학생의 학년, 성별과 자습실 이용 가능한 학년, 성별이 같으면") {
            val studyRoom = createStudyRoomStub(availableGrade = 3, availableSex = Sex.MALE)
            val grade = 3
            val sex = Sex.MALE

            it("자습실을 이용할 수 있다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(grade, sex)
                }
            }
        }

        context("자습실 이용 가능한 학년이 0이면") {
            val studyRoom = createStudyRoomStub(availableGrade = 0, availableSex = Sex.MALE)
            val grade = 3
            val sex = Sex.MALE

            it("모든 학년이 자습실을 이용할 수 있다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(grade, sex)
                }
            }
        }

        context("자습실 이용 가능한 성별이 ALL이면") {
            val studyRoom = createStudyRoomStub(availableGrade = 3, availableSex = Sex.ALL)
            val grade = 3
            val sex = Sex.MALE

            it("같은 학년의 모든 학생이 자습실을 이용할 수 있다") {
                shouldNotThrowAny {
                    studyRoom.checkIsAvailableGradeAndSex(grade, sex)
                }
            }
        }

        context("자습실 이용 가능한 학년이 다르면") {
            val studyRoom = createStudyRoomStub(availableGrade = 3, availableSex = Sex.MALE)
            val grade = 2
            val sex = Sex.MALE

            it("자습실을 이용할 수 없다") {
                shouldThrow<StudyRoomAvailableGradeMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(grade, sex)
                }
            }
        }

        context("자습실 이용 가능한 성별이 다르면") {
            val studyRoom = createStudyRoomStub(availableGrade = 3, availableSex = Sex.MALE)
            val grade = 3
            val sex = Sex.FEMALE

            it("자습실을 이용할 수 없다") {
                shouldThrow<StudyRoomAvailableSexMismatchException> {
                    studyRoom.checkIsAvailableGradeAndSex(grade, sex)
                }
            }
        }
    }
})
