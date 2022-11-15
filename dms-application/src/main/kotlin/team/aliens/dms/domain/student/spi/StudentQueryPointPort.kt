package team.aliens.dms.domain.student.spi

import java.util.UUID

interface StudentQueryPointPort {

    fun queryTotalBonusPoint(studentId: UUID): Int

    fun queryTotalMinusPoint(studentId: UUID): Int

}