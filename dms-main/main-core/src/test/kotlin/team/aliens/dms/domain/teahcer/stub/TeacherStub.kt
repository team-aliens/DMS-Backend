package team.aliens.dms.domain.teahcer.stub

import team.aliens.dms.domain.teacher.model.Teacher
import java.util.UUID

internal fun createTeacherStub(
    id: UUID = UUID.randomUUID(),
    name: String = "general_teacher",
    grade: Int? = null
) = Teacher(
    id = id,
    name = name,
    grade = grade
)