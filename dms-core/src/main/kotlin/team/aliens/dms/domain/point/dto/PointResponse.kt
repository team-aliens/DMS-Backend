package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.AllPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import java.util.UUID

data class PointHistoryResponse(
    val totalPoint: Int? = null,
    val pointHistories: List<PointHistoryVO>
)

data class AllPointHistoryResponse(
    val pointHistories: List<AllPointHistoryVO>
)

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
