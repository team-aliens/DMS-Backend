package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationAlreadyExistsException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Service
class CheckOutingServiceImpl(
    private val queryOutingApplicationPort: QueryOutingApplicationPort
) : CheckOutingService {

    override fun checkOutingApplicationExistsByOutAtAndStudentId(outAt: LocalDate, studentId: UUID) {
        if(queryOutingApplicationPort.existOutingApplicationByOutAtAndStudentId(outAt, studentId)) {
            throw OutingApplicationAlreadyExistsException
        }
    }

    override fun checkOutingApplicationTimeAvailable(outAt: LocalDate) {
        if (outAt.dayOfWeek != DayOfWeek.SUNDAY || outAt.dayOfWeek != DayOfWeek.SATURDAY) {
            throw OutingAvailableTimeMismatchException
        }
    }
}
