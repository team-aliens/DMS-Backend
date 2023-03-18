package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.studyroom.model.Seat
import java.util.UUID

interface StudentCommandStudyRoomPort {

    fun saveSeat(seat: Seat): Seat

    fun deleteSeatApplicationByStudentId(studentId: UUID)
}
