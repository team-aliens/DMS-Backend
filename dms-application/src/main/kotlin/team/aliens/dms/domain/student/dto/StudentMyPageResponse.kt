package team.aliens.dms.domain.student.dto

data class StudentMyPageResponse(
    val schoolName: String,
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val bonusPoint: Int,
    val minusPoint: Int,
    val phrase: String
)
