package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.point.model.PointHistory
import java.util.UUID

interface StudentQueryPointHistoryPort {

    fun queryTotalBonusPoint(studentId: UUID): Int

    fun queryTotalMinusPoint(studentId: UUID): Int

}