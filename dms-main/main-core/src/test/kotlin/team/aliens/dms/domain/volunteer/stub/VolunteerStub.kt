package team.aliens.dms.domain.volunteer.stub

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

internal fun createVolunteerStub(
    id: UUID = UUID.randomUUID(),
    name: String = "3층 분리수거",
    score: Int = 4,
    optionalScore: Int = 2,
    maxApplicants: Int = 3,
    availableSex: Sex = Sex.MALE,
    availableGrade: AvailableGrade = AvailableGrade.SECOND,
    schoolId: UUID = UUID.randomUUID(),
) = Volunteer(
    id = id,
    name = name,
    score = score,
    optionalScore = optionalScore,
    maxApplicants = maxApplicants,
    availableSex = availableSex,
    availableGrade = availableGrade,
    schoolId = schoolId
)

internal fun createVolunteerApplicationStub(
    id: UUID = UUID.randomUUID(),
    studentId: UUID = UUID.randomUUID(),
    volunteerId: UUID = UUID.randomUUID(),
    approved: Boolean = false,
) = VolunteerApplication(
    id = id,
    studentId = studentId,
    volunteerId = volunteerId,
    approved = approved
)
