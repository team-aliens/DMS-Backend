package team.aliens.dms.domain.school.dto

import java.time.LocalDate
import java.util.UUID
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School

class CreateSchoolRequest(
    val schoolName: String,
    val schoolAddress: String,
    val mealService: Boolean,
    val noticeService: Boolean,
    val pointService: Boolean,
    val studyRoomService: Boolean,
    val remainService: Boolean
) {
    fun toSchool() =
        School(
            name = schoolName,
            code = StringUtil.randomNumber(School.SCHOOL_CODE_SIZE),
            question = "우리 학교 이름은?",
            answer = schoolName,
            address = schoolAddress,
            contractStartedAt = LocalDate.now()
        )

    fun toAvailableFeature(schoolId: UUID) =
        AvailableFeature(
            schoolId = schoolId,
            mealService = mealService,
            noticeService = noticeService,
            pointService = pointService,
            studyRoomService = studyRoomService,
            remainService = remainService
        )
}
