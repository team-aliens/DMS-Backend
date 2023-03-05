package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface QueryStudentPort {

    fun existsStudentByGradeAndClassRoomAndNumber(grade: Int, classRoom: Int, number: Int): Boolean

    fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student?

    fun queryStudentById(studentId: UUID): Student?

    fun existsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): Boolean
}
