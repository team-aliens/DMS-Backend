package team.aliens.dms.domain.studyroom.model

import java.util.UUID
import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.student.model.Sex

@Aggregate
data class StudyRoom(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String,

    val floor: Int,

    val widthSize: Int,

    val heightSize: Int,

    val inUseHeadcount: Int? = 0,

    val availableHeadcount: Int,

    val availableSex: Sex,

    val availableGrade: Int,

    val eastDescription: String,

    val westDescription: String,

    val southDescription: String,

    val northDescription: String

)
