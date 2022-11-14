package team.aliens.dms.domain.manager.spi

import java.util.UUID

interface ManagerQueryPointPort {

    fun queryTotalBonusPoint(studentId: UUID): Int

    fun queryTotalMinusPoint(studentId: UUID): Int

}