package team.aliens.dms.domain.student.service

import team.aliens.dms.domain.student.model.Student

interface CommandStudentService {

    fun saveStudent(student: Student): Student

    fun saveAllStudent(students: List<Student>)

    fun deleteStudent(student: Student)
}
