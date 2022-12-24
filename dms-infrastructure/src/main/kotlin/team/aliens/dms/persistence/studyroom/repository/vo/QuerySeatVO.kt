package team.aliens.dms.persistence.studyroom.repository.vo

import com.querydsl.core.annotations.QueryProjection
import java.util.UUID
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
import team.aliens.dms.domain.studyroom.model.SeatStatus

class QuerySeatVO @QueryProjection constructor(
    seatId: UUID,
    widthLocation: Int,
    heightLocation: Int,
    number: Int?,
    status: SeatStatus,
    typeId: UUID?,
    typeName: String?,
    typeColor: String?,
    studentId: UUID?,
    studentName: String?
) : SeatVO(
    seatId = seatId,
    widthLocation = widthLocation,
    heightLocation = heightLocation,
    number = number,
    status = status,
    typeId = typeId,
    typeName = typeName,
    typeColor = typeColor,
    studentId = studentId,
    studentName = studentName
)