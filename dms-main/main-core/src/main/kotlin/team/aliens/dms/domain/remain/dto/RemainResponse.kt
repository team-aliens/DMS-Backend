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
                RemainOptionResponse.ofDetail(it, remainOptionId)
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

        fun of(remainOption: RemainOption) = remainOption.run {
            RemainOptionResponse(
                id = id,
                title = title
            )
        }

        fun ofDetail(remainOption: RemainOption, remainOptionId: UUID?) = remainOption.run {
            of(this).copy(
                description = description,
                isApplied = id == remainOptionId
            )
        }
    }
}

data class RemainAvailableTimeResponse(
    val startDayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endDayOfWeek: DayOfWeek,
    val endTime: LocalTime
)
