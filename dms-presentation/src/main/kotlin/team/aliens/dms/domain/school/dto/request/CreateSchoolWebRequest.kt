package team.aliens.dms.domain.school.dto.request

class CreateSchoolWebRequest(
    val schoolName: String,
    val schoolAddress: String,
    val mealService: Boolean,
    val noticeService: Boolean,
    val pointService: Boolean,
    val studyRoomService: Boolean,
    val remainService: Boolean
)