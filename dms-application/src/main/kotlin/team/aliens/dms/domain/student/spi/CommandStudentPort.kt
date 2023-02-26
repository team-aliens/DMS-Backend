package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.model.VerifiedStudent

interface CommandStudentPort {

    fun saveStudent(student: Student): Student

    fun saveAllVerifiedStudent(verifiedStudents: List<VerifiedStudent>)

    fun deleteVerifiedStudent(verifiedStudent: VerifiedStudent)

}
