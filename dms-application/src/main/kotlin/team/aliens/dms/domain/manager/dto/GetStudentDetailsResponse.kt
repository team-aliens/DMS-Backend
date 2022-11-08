package team.aliens.dms.domain.manager.dto

import java.util.UUID

data class GetStudentDetailsResponse(
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val bonusPoint: Int,
    val minusPoint: Int,
    val roomNumber: Int,
    val roomMates: List<RoomMate>

) {
    data class RoomMate(
        val id: UUID,
        val name: String,
        val profileImageUrl: String
    )
}