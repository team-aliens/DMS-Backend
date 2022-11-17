package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.manager.model.Manager
import java.util.UUID

interface QueryManagerPort {

    fun queryManagerById(managerId: UUID): Manager?

}