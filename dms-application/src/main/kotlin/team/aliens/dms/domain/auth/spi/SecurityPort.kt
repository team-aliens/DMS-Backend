package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort

interface SecurityPort : ManagerSecurityPort, StudentSecurityPort, MealSecurityPort {
    fun isPasswordMatch(rawPassword: String, encodedPassword: String): Boolean
}