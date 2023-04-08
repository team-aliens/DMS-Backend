package team.aliens.dms.domain.meal.spi

import java.util.UUID
import team.aliens.dms.domain.student.model.Student

interface MealQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentByUserId(userId: UUID): Student?
}
