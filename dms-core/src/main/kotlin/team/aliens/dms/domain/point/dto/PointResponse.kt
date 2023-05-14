package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

data class PointHistoryResponse(
    val totalPoint: Int? = null,
    val pointHistories: List<PointHistoryDto>
) {
    data class PointHistoryDto(
        val pointHistoryId: UUID,
        val date: LocalDate,
        val type: PointType,
        val name: String,
        val score: Int
    )
}

data class AllPointHistoryResponse(
    val pointHistories: List<PointHistory>
) {
    data class PointHistory(
        val pointHistoryId: UUID,
        val studentName: String,
        val studentGcn: String,
        val date: LocalDate,
        val pointName: String,
        val pointType: PointType,
        val pointScore: Int
    )
}

data class PointOptionsResponse(
    val pointOptions: List<PointOptionResponse>
) {
    data class PointOptionResponse(
        val pointOptionId: UUID,
        val type: PointType,
        val score: Int,
        val name: String
    )

    companion object {
        fun of(pointOptions: List<PointOption>) = PointOptionsResponse(
            pointOptions = pointOptions.map {
                PointOptionResponse(
                    pointOptionId = it.id,
                    type = it.type,
                    score = it.score,
                    name = it.name
                )
            }
        )
    }
}

data class ExportAllPointHistoryResponse(
    val fileName: String,
    val file: ByteArray
)
