package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface QueryStudentPort {
    fun existsByGcn(grade: Int, classRoom: Int, number: Int): Boolean
    fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student?
}