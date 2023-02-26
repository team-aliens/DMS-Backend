package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.student.model.Student

interface ManagerCommandStudentPort {

    fun deleteStudent(student: Student)
}
