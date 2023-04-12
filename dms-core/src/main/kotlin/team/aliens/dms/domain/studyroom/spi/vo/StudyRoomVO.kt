package team.aliens.dms.domain.studyroom.spi.vo

import team.aliens.dms.domain.student.model.Sex
import java.util.UUID

open class StudyRoomVO(
    val id: UUID,
    val floor: Int,
    val name: String,
    val availableGrade: Int,
    val availableSex: Sex,
    val inUseHeadcount: Int,
    val totalAvailableSeat: Int
)
