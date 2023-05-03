package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService,
) {

    fun execute(timeSlotId: UUID): Pair<List<StudyRoomVO>, UUID?> {

        val student = studentService.getCurrentStudent()
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)

        val appliedStudyRoomId = studyRoomService.getAppliedSeat(student.id, timeSlot.id)?.studyRoomId
        val studyRooms = studyRoomService.getStudyRoomVOs(timeSlot.id, student.grade, student.sex)

        return Pair(studyRooms, appliedStudyRoomId)
    }
}
