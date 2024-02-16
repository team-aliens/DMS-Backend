package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingApplication
import java.time.LocalDate
import java.util.UUID

interface QueryOutingApplicationPort {

    fun existOutingApplicationByOutAtAndStudentId(outAt: LocalDate, studentId: UUID): Boolean

    fun queryOutingApplicationById(outingApplicationId: UUID): OutingApplication?
}
