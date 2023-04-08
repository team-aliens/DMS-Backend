package team.aliens.dms.domain.manager.spi

import java.util.UUID
import team.aliens.dms.domain.manager.model.Manager

interface QueryManagerPort {

    fun queryManagerById(managerId: UUID): Manager?

    fun existsManagerById(managerId: UUID): Boolean
}
