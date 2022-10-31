package team.aliens.dms.domain.manager.spi

import java.util.UUID

interface ManagerQueryPointHistoryPort {
    fun queryPointScore(studentId: UUID, isBonus: Boolean): Int
}