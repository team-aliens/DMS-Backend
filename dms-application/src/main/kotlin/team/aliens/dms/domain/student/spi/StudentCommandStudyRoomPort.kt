package team.aliens.dms.domain.student.spi

import java.util.UUID

interface StudentCommandStudyRoomPort {

    fun deleteSeatApplicationByStudentId(studentId: UUID)
}
