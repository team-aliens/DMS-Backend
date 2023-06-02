package team.aliens.dms.domain.remain.dto

import team.aliens.dms.domain.remain.model.RemainOption
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

data class RemainOptionsResponse(
    val remainOptions: List<RemainOptionResponse>
) {
    companion object {

        fun of(remainOptions: List<RemainOption>, remainOptionId: UUID?) = RemainOptionsResponse(
            remainOptions = remainOptions.map {
                RemainOptionResponse.of(it.id, it.title).copy(
                    description = it.description,
                    isApplied = it.id == remainOptionId
                )
            }
        )
    }
}

data class RemainOptionResponse(
    val id: UUID,
    val title: String,
    val description: String? = null,
    val isApplied: Boolean? = null
) {
    companion object {

        fun of(remainOptionId: UUID, title: String) = RemainOptionResponse(
            id = remainOptionId,
            title = title
        )
    }
}

data class RemainAvailableTimeResponse(
    val startDayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endDayOfWeek: DayOfWeek,
    val endTime: LocalTime
)
