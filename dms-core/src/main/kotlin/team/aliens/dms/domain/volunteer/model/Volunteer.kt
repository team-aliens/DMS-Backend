package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.student.model.Sex
import java.util.UUID

@Aggregate
data class Volunteer(

    val id: UUID = UUID(0, 0),

    val name: String,

    val content: String,

    val score: Int,

    val optionalScore: Int = 0,

    val maxApplicants: Int,

    val sexCondition: Sex,

    val gradeCondition: GradeCondition,
)
