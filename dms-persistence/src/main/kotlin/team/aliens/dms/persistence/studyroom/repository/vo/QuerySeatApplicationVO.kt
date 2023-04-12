package team.aliens.dms.persistence.studyroom.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import java.util.UUID

class QuerySeatApplicationVO @QueryProjection constructor(
    seatId: UUID,
    widthLocation: Int,
    heightLocation: Int,
    number: Int?,
    status: SeatStatus,
    typeId: UUID?,
    typeName: String?,
    typeColor: String?,
    studentId: UUID?,
    studentName: String?,
    studentGrade: Int?,
    studentClassRoom: Int?,
    studentNumber: Int?,
    studentProfileImageUrl: String?
) : SeatApplicationVO(
    seatId = seatId,
    widthLocation = widthLocation,
    heightLocation = heightLocation,
    number = number,
    status = status,
    typeId = typeId,
    typeName = typeName,
    typeColor = typeColor,
    studentId = studentId,
    studentName = studentName,
    studentGrade = studentGrade,
    studentClassRoom = studentClassRoom,
    studentNumber = studentNumber,
    studentProfileImageUrl = studentProfileImageUrl
)
