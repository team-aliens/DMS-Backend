package team.aliens.dms.domain.studyroom.dto

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.SeatStatus

open class SeatVO(
    val seatId: UUID,
    val widthLocation: Int,
    val heightLocation: Int,
    val number: Int?,
    val status: SeatStatus,
    val typeId: UUID?,
    val typeName: String?,
    val typeColor: String?,
    val studentId: UUID?,
    val studentName: String?
)