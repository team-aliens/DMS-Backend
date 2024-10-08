package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.exception.VolunteerNotAvailableException
import java.util.UUID

@Aggregate
data class Volunteer(

    val id: UUID = UUID(0, 0),

    val name: String,

    val score: Int,

    val optionalScore: Int = 0,

    val maxApplicants: Int,

    val availableSex: Sex,

    val availableGrade: AvailableGrade,

    override val schoolId: UUID
) : SchoolIdDomain {

    fun isAvailable(student: Student): Boolean {
        return this.availableGrade.grades.contains(student.grade) &&
            (this.availableSex == student.sex || this.availableSex == Sex.ALL)
    }

    fun checkAvailable(student: Student) {
        if (!isAvailable(student)) throw VolunteerNotAvailableException
    }
}
