package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeAlreadyExistsException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.outing.exception.OutingTypeAlreadyExistsException
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingApplicationPort
import team.aliens.dms.domain.outing.spi.QueryOutingAvailableTimePort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Service
class CheckOutingServiceImpl(
    private val queryOutingApplicationPort: QueryOutingApplicationPort,
    private val queryOutingAvailableTimePort: QueryOutingAvailableTimePort,
    private val queryOutingTypePort: QueryOutingTypePort
) : CheckOutingService {

    override fun checkOutingApplicationAvailable(
        studentId: UUID,
        outingDate: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        checkOutingAvailableTime(outingDate, outingTime, arrivalTime)
    }

    private fun checkOutingAvailableTime(
        outingDate: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        queryOutingAvailableTimePort.queryOutingAvailableTimeByDayOfWeek(outingDate.dayOfWeek)
            ?.checkAvailable(outingDate.dayOfWeek, outingTime, arrivalTime)
            ?: throw OutingAvailableTimeMismatchException
    }

    override fun checkOutingTypeExists(outingType: OutingType) {
        if (queryOutingTypePort.existsOutingType(outingType)) {
            throw OutingTypeAlreadyExistsException
        }
    }

    override fun checkOutingAvailableTime(
        dayOfWeek: DayOfWeek,
        startTime: LocalTime,
        endTime: LocalTime
    ) {
        val existingTimes = queryOutingAvailableTimePort.queryOutingAvailableTimesByDayOfWeek(dayOfWeek)

        for (existingTime in existingTimes) {
            if (timesOverlap(startTime, endTime, existingTime.outingTime, existingTime.arrivalTime)) {
                throw OutingAvailableTimeAlreadyExistsException
            }
        }
    }

    private fun timesOverlap(
        newStartTime: LocalTime,
        newEndTime: LocalTime,
        startTime: LocalTime,
        endTime: LocalTime
    ): Boolean {
        return !(newEndTime <= startTime || newStartTime >= endTime)
    }
}
