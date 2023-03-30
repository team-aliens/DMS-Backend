package team.aliens.dms.domain.studyroom.spi.vo

import team.aliens.dms.domain.studyroom.model.SeatStatus
import java.util.UUID

open class SeatApplicationVO(
    val seatId: UUID,
    val widthLocation: Int,
    val heightLocation: Int,
    val number: Int?,
    val status: SeatStatus,
    val typeId: UUID?,
    val typeName: String?,
    val typeColor: String?,
    val studentId: UUID?,
    val studentName: String?,
    val studentGrade: Int?,
    val studentClassRoom: Int?,
    val studentNumber: Int?,
    val studentProfileImageUrl: String?
)
