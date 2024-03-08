package team.aliens.dms.domain.outing.dto

import com.fasterxml.jackson.annotation.JsonInclude
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import team.aliens.dms.domain.studyroom.dto.StudyRoomResponse
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
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
    val outAt: LocalDate,
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
                    outAt = outAt,
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

data class OutingApplicationHistoryResponse(
    val outings: List<OutingHistoryVO>
) {
    companion object {
        fun of(outingHistories: List<OutingHistoryVO>) = OutingApplicationHistoryResponse(
            outings = outingHistories.map {
                it.run {
                    OutingHistoryVO(
                        outingApplicationId = outingApplicationId,
                        name = name,
                        outingType = outingType,
                        companionCount = companionCount,
                        outingTime = outingTime,
                        arrivalTime = arrivalTime
                    )
                }
            }
        )
    }
}
