package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface MealQueryUserPort {
    fun queryByUserId(id: UUID): User?
}