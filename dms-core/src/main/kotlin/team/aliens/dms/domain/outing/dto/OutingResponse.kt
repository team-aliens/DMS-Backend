package team.aliens.dms.domain.outing.dto

import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import java.time.LocalDate
import java.time.LocalTime

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
                    outingCompanions = outingCompanions
                )
            }
    }
}
