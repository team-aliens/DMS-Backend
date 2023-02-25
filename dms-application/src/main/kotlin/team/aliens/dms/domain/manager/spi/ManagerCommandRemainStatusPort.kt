package team.aliens.dms.domain.manager.spi

import java.util.UUID

interface ManagerCommandRemainStatusPort {
    fun deleteByStudentId(studentId: UUID)
}