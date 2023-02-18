package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.manager.model.Manager
import java.util.*

interface PointQueryManagerPort {
    fun queryManagerById(managerId: UUID): Manager?
}