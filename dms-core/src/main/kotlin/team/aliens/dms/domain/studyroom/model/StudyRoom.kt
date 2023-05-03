package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException
import java.util.UUID

@Aggregate
data class StudyRoom(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val name: String,

    val floor: Int,

    val widthSize: Int,

    val heightSize: Int,

    val availableHeadcount: Int,

    val availableSex: Sex,

    val availableGrade: Int,

    val eastDescription: String,

    val westDescription: String,

    val southDescription: String,

    val northDescription: String

) : SchoolIdDomain {

    fun checkIsAvailableGradeAndSex(grade: Int, sex: Sex) {
        if (availableGrade != 0 && availableGrade != grade) {
            throw StudyRoomAvailableGradeMismatchException
        }

        if (availableSex != Sex.ALL && availableSex != sex) {
            throw StudyRoomAvailableSexMismatchException
        }
    }

    companion object {
        fun precessName(floor: Int, name: String) = "$floor-$name"
    }
}
