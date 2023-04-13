package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.student.model.Student

interface CommandStudentPort {

    fun saveStudent(student: Student): Student

    fun saveAllStudent(students: List<Student>)

    fun deleteStudent(student: Student)
}
