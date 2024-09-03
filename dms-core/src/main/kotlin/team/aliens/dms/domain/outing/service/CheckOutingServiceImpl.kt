package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.exception.OutingApplicationAlreadyExistsException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeAlreadyExistsException
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.outing.exception.OutingTypeAlreadyExistsException
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.QueryOutingAvailableTimePort
import team.aliens.dms.domain.outing.spi.QueryOutingCompanionPort
import team.aliens.dms.domain.outing.spi.QueryOutingTypePort
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Service
class CheckOutingServiceImpl(
    private val queryOutingCompanionPort: QueryOutingCompanionPort,
    private val queryOutingAvailableTimePort: QueryOutingAvailableTimePort,
    private val queryOutingTypePort: QueryOutingTypePort
) : CheckOutingService {

    override fun checkOutingApplicationAvailable(
        studentId: UUID,
        schoolId: UUID,
        outingDate: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        checkOutingApplicationExists(studentId)
        checkOutingAvailableTime(schoolId, outingDate, outingTime, arrivalTime)
    }

    private fun checkOutingApplicationExists(studentId: UUID) {
        if (queryOutingCompanionPort.existsOutingCompanionById(studentId)) {
            throw OutingApplicationAlreadyExistsException
        }
    }

    private fun checkOutingAvailableTime(
        schoolId: UUID,
        outingDate: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        queryOutingAvailableTimePort.queryOutingAvailableTimeByDayOfWeekAndSchoolId(outingDate.dayOfWeek, schoolId)
            ?.checkAvailable(outingDate.dayOfWeek, outingTime, arrivalTime)
            ?: throw OutingAvailableTimeMismatchException
    }

    override fun checkOutingTypeExists(outingType: OutingType) {
        if (queryOutingTypePort.existsOutingType(outingType)) {
            throw OutingTypeAlreadyExistsException
        }
    }

    override fun checkOutingAvailableTimeOverlap(
        schoolId: UUID,
        dayOfWeek: DayOfWeek,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    ) {
        val existingTimes = queryOutingAvailableTimePort.queryOutingAvailableTimesByDayOfWeekAndSchoolId(dayOfWeek, schoolId)

        for (existingTime in existingTimes) {
            if (existingTime.timesOverlap(outingTime, arrivalTime)) {
                throw OutingAvailableTimeAlreadyExistsException
            }
        }
    }
}
