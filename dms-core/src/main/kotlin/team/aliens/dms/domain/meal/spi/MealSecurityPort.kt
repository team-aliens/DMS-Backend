package team.aliens.dms.domain.meal.spi

import java.util.UUID

interface MealSecurityPort {

    fun getCurrentUserId(): UUID
}
