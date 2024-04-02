package team.aliens.dms.domain.outing.dto

import com.fasterxml.jackson.annotation.JsonInclude
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingAvailableTimeVO
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class GetAllOutingTypeTitlesResponse(
    val titles: List<String>
)

data class ExportAllOutingApplicationsResponse(
    val file: ByteArray,
    val fileName: String
)

data class GetCurrentOutingApplicationResponse(
    val outingDate: LocalDate,
    val outingTypeTitle: String,
    val status: OutingStatus,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    @field:JsonInclude(JsonInclude.Include.ALWAYS)
    val reason: String?,
    val outingCompanions: List<String>
) {
    companion object {
        fun of(currentOutingApplicationVO: CurrentOutingApplicationVO) =
            currentOutingApplicationVO.run {
                GetCurrentOutingApplicationResponse(
                    outingDate = outingDate,
                    outingTypeTitle = outingTypeTitle,
                    status = status,
                    outingTime = outingTime,
                    arrivalTime = arrivalTime,
                    reason = reason,
                    outingCompanions = outingCompanions
                )
            }
    }
}

data class OutingApplicationHistoriesResponse(
    val outings: List<OutingHistoryVO>
)

data class OutingAvailableTimesResponse(
    val outingAvailableTimes: List<OutingAvailableTimeVO>
)

data class OutingHistoryDetailsResponse(
    val studentName: String,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val outingStatus: OutingStatus,
    @field:JsonInclude(JsonInclude.Include.ALWAYS)
    val reason: String?,
    val outingType: String,
    val students: List<OutingCompanionsResponse>
) {
    companion object {
        fun of(
            outingHistory: OutingApplication,
            outingCompanions: List<OutingCompanionDetailsVO>,
            student: Student
        ): OutingHistoryDetailsResponse {
            return OutingHistoryDetailsResponse(
                studentName = student.name,
                outingTime = outingHistory.outingTime,
                arrivalTime = outingHistory.arrivalTime,
                outingStatus = outingHistory.status,
                reason = outingHistory.reason,
                outingType = outingHistory.outingTypeTitle,
                students = outingCompanions.map {
                    OutingCompanionsResponse.of(it)
                }
            )
        }
    }
}

data class OutingCompanionsResponse(
    val id: UUID,
    val studentName: String,
    val roomNumber: String,
    val studentGcn: String
) {
    companion object {
        fun of(outingCompanions: OutingCompanionDetailsVO): OutingCompanionsResponse {
            return OutingCompanionsResponse(
                id = outingCompanions.id,
                studentName = outingCompanions.studentName,
                roomNumber = outingCompanions.roomNumber,
                studentGcn = outingCompanions.studentGcn
            )
        }
    }
}
