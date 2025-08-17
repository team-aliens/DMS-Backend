package team.aliens.dms.persistence.studyroom.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import java.util.UUID

class QueryStudyRoomVO @QueryProjection constructor(
    id: UUID,
    floor: Int,
    name: String,
    availableGrade: Int,
    availableSex: Sex,
    inUseHeadcount: Int,
    totalAvailableSeat: Int
) : StudyRoomVO(
    id = id,
    floor = floor,
    name = name,
    availableGrade = availableGrade,
    availableSex = availableSex,
    inUseHeadcount = inUseHeadcount,
    totalAvailableSeat = totalAvailableSeat
)
