package team.aliens.dms.persistence.student.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.common.annotation.EncryptType
import team.aliens.dms.common.annotation.EncryptedColumn
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO

class QueryStudentWithPointVO @QueryProjection constructor(
    @EncryptedColumn(type = EncryptType.SYMMETRIC)
    override val name: String,
    grade: Int,
    classRoom: Int,
    number: Int,
    bonusTotal: Int?,
    minusTotal: Int?,
) : StudentWithPointVO(
    name = name,
    grade = grade,
    classRoom = classRoom,
    number = number,
    bonusTotal = bonusTotal ?: 0,
    minusTotal = minusTotal ?: 0
)
