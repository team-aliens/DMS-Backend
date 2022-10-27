package team.aliens.dms.domain.manager.spi

import java.util.UUID

interface ManagerQueryPointHistoryPort {
    fun getPointScore(studentId: UUID, isBonus: Boolean): Int
}