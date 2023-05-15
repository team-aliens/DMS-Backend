package team.aliens.dms.domain.school.dto

import team.aliens.dms.domain.school.model.School
import java.util.UUID

data class SchoolsResponse(
    val schools: List<SchoolElement>
) {
    data class SchoolElement(
        val id: UUID,
        val name: String,
        val address: String
    )

    companion object {
        fun of(schools: List<School>) = SchoolsResponse(
            schools = schools.map {
                SchoolElement(
                    id = it.id,
                    name = it.name,
                    address = it.address
                )
            }
        )
    }
}

data class AvailableFeaturesResponse(
    val mealService: Boolean,
    val noticeService: Boolean,
    val pointService: Boolean,
    val studyRoomService: Boolean,
    val remainService: Boolean
)

data class NeisSchoolResponse(
    val sdSchoolCode: String,
    val regionCode: String
)

data class SchoolQuestionResponse(
    val question: String
)

data class SchoolIdResponse(
    val schoolId: UUID
)

data class ReissueSchoolCodeResponse(
    val code: String
)
