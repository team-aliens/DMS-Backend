package team.aliens.dms.domain.outing.spi

import java.time.LocalDate
import java.util.UUID

interface QueryOutingApplicationPort {

    fun existOutingApplicationByOutAtAndStudentId(outAt: LocalDate, studentId: UUID): Boolean

}
