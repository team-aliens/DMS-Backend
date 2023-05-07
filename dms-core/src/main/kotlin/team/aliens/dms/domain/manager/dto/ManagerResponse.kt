package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.school.model.School
import java.util.UUID

data class ManagerDetailsResponse(
    val schoolId: UUID,
    val schoolName: String,
    val code: String,
    val question: String,
    val answer: String
) {
    companion object {
        fun of(school: School) = ManagerDetailsResponse(
            schoolId = school.id,
            schoolName = school.name,
            code = school.code,
            question = school.question,
            answer = school.answer
        )
    }
}

data class ManagerEmailResponse(
    val email: String
)
