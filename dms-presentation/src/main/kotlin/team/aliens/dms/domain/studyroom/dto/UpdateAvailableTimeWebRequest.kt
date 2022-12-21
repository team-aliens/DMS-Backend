package team.aliens.dms.domain.studyroom.dto

import java.time.LocalTime

data class UpdateAvailableTimeWebRequest(
    val startAt: LocalTime,
    val endAt: LocalTime
)
