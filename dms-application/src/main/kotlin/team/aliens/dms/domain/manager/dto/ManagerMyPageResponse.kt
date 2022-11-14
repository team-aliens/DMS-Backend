package team.aliens.dms.domain.manager.dto

import java.util.UUID

data class ManagerMyPageResponse(
    val schoolId: UUID,
    val schoolName: String,
    val code: String,
    val question: String,
    val answer: String
)