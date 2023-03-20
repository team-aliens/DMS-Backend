package team.aliens.dms.domain.school.stub

import team.aliens.dms.domain.school.model.School
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
