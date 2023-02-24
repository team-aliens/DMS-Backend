package team.aliens.dms.domain.point.dto

data class UpdatePointOptionRequest(
    val name: String,
    val type: String,
    val score: Int
)
