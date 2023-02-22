package team.aliens.dms.domain.point.dto

data class CreatePointOptionRequest(
    val name: String,
    val type: String,
    val score: Int
)
