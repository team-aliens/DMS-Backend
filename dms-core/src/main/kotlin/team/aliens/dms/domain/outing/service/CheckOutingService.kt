package team.aliens.dms.domain.outing.service

import java.time.LocalDate
import java.util.UUID

interface CheckOutingService {

    fun checkOutingApplicationExistsByOutAtAndStudentId(outAt: LocalDate, studentId: UUID)

    fun checkOutingApplicationTimeAvailable(outAt: LocalDate)

    fun checkOutingAvailable(outAt: LocalDate, studentId: UUID) {
        checkOutingApplicationTimeAvailable(outAt)
        checkOutingApplicationExistsByOutAtAndStudentId(outAt, studentId)
    }
}
