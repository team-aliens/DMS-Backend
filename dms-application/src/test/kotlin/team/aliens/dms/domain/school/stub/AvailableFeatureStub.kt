package team.aliens.dms.domain.school.stub

import team.aliens.dms.domain.school.model.AvailableFeature
import java.util.UUID

internal fun createAvailableFeatureStub(
    schoolId: UUID = UUID.randomUUID(),
    mealService: Boolean = true,
    studyRoomService: Boolean = true,
    remainService: Boolean = true,
    pointService: Boolean = true,
    noticeService: Boolean = true,
) = AvailableFeature(
    schoolId = schoolId,
    mealService = mealService,
    studyRoomService = studyRoomService,
    remainService = remainService,
    pointService = pointService,
    noticeService = noticeService
)
