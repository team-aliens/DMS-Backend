package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort

interface StudentPort :
    QueryStudentPort,
    CommandStudentPort,
    AuthQueryStudentPort,
    MealQueryStudentPort {
}