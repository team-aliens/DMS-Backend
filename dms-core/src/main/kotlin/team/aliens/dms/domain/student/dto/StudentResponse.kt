package team.aliens.dms.domain.student.dto

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student

data class StudentResponse(
    val schoolName: String,
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val sex: Sex,
    val bonusPoint: Int,
    val minusPoint: Int,
    val phrase: String
) {
    companion object {
        fun of(
            schoolName: String,
            student: Student,
            bonusPoint: Int,
            minusPoint: Int,
            phrase: String,
        ) = StudentResponse(
            schoolName = schoolName,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = phrase
        )
    }
}

data class StudentEmailResponse(
    val email: String
)

data class StudentNameResponse(
    val name: String
)
