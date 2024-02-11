package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationAlreadyExistsException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingAvailableTimePort
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Service
class CheckOutingServiceImpl(
    private val queryOutingApplicationPort: QueryOutingApplicationPort,
    private val queryOutingAvailableTimePort: QueryOutingAvailableTimePort,
) : CheckOutingService {

    override fun checkOutingApplicationAvailable(
        studentId: UUID,
        outAt: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        checkOutingAvailableTime(outAt, outingTime, arrivalTime)
        checkOutingApplicationExistsByOutAtAndStudentId(outAt, studentId)
    }

    private fun checkOutingAvailableTime(
        outAt: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        queryOutingAvailableTimePort.queryOutingAvailableTimeByDayOfWeek(outAt.dayOfWeek)
            ?.checkAvailable(outAt.dayOfWeek, outingTime, arrivalTime)
            ?: throw OutingAvailableTimeMismatchException
    }

    private fun checkOutingApplicationExistsByOutAtAndStudentId(outAt: LocalDate, studentId: UUID) {
        if (queryOutingApplicationPort.existOutingApplicationByOutAtAndStudentId(outAt, studentId)) {
            throw OutingApplicationAlreadyExistsException
        }
    }

    override fun checkOutingTypeExists(outingType: OutingType) {
        if (queryOutingTypePort.exists(outingType)) {
            throw OutingTypeAlreadyExistsException
        }
    }
}
