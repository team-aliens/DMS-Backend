package team.aliens.dms.domain.student.spi

import java.util.UUID

interface StudentCommandRemainStatusPort {
    fun deleteByStudentId(studentId: UUID)
}
