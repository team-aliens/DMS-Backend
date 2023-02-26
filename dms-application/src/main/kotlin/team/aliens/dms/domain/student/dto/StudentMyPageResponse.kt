package team.aliens.dms.domain.student.dto

import team.aliens.dms.domain.student.model.Sex

data class StudentMyPageResponse(
    val schoolName: String,
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val sex: Sex,
    val bonusPoint: Int,
    val minusPoint: Int,
    val phrase: String
)
