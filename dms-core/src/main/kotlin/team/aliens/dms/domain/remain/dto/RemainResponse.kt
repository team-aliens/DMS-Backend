package team.aliens.dms.domain.remain.dto

import team.aliens.dms.domain.remain.model.RemainOption
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

data class RemainOptionsResponse(
    val remainOptions: List<RemainOptionElement>
) {
    data class RemainOptionElement(
        val id: UUID,
        val title: String,
        val description: String,
        val isApplied: Boolean
    )
    companion object {
        fun of(remainOptions: List<RemainOption>, remainOptionId: UUID?) = RemainOptionsResponse(
            remainOptions = remainOptions.map {
                RemainOptionElement(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isApplied = it.id == remainOptionId
                )
            }
        )
    }
}

data class RemainAvailableTimeResponse(
    val startDayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endDayOfWeek: DayOfWeek,
    val endTime: LocalTime
)

data class CurrentAppliedRemainOptionResponse(
    val remainOptionId: UUID,
    val title: String
)
