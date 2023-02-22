package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainStatus

interface CommandRemainStatusPort {

    fun saveRemainStatus(remainStatus: RemainStatus): RemainStatus
}