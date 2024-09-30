package team.aliens.dms.domain.school.stub

import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.volunteer.exception.VolunteerErrorCode
import java.time.LocalDate
import java.util.UUID

internal fun createSchoolStub(
    id: UUID = UUID.randomUUID(),
    name: String = "test name",
    code: String = "test code",
    question: String = "test question",
    answer: String = "test answer",
    address: String = "test address",
    contractStartedAt: LocalDate = LocalDate.now(),
    contractEndedAt: LocalDate? = LocalDate.now(),
) = School(
    id = id,
    name = name,
    code = code,
    question = question,
    answer = answer,
    address = address,
    contractStartedAt = contractStartedAt,
    contractEndedAt = contractEndedAt
)

internal fun createAvailableFeatureStub(
    schoolId: UUID = UUID.randomUUID(),
    mealService: Boolean = true,
    studyRoomService: Boolean = true,
    remainService: Boolean = true,
    pointService: Boolean = true,
    noticeService: Boolean = true,
    outingService: Boolean = true,
    volunteerService: Boolean = true
) = AvailableFeature(
    schoolId = schoolId,
    mealService = mealService,
    studyRoomService = studyRoomService,
    remainService = remainService,
    pointService = pointService,
    noticeService = noticeService,
    outingService = outingService,
    volunteerService = volunteerService
)
