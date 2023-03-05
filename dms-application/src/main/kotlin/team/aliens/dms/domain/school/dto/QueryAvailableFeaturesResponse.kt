package team.aliens.dms.domain.school.dto

data class QueryAvailableFeaturesResponse(
    val mealService: Boolean,
    val noticeService: Boolean,
    val pointService: Boolean,
    val studyRoomService: Boolean,
    val remainService: Boolean
)
