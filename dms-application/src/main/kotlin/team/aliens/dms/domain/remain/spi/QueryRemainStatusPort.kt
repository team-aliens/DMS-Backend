package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainStatus
import java.util.UUID

interface QueryRemainStatusPort {

    fun queryRemainStatusById(userId: UUID): RemainStatus?

}