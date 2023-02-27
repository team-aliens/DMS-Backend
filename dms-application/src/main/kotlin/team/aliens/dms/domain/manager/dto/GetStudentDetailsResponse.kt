package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.student.model.Sex
import java.util.UUID

data class GetStudentDetailsResponse(
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val sex: Sex,
    val bonusPoint: Int,
    val minusPoint: Int,
    val roomNumber: String,
    val roomMates: List<RoomMate>

) {
    data class RoomMate(
        val id: UUID,
        val name: String,
        val profileImageUrl: String
    )
}
