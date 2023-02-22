package team.aliens.dms.domain.remain.spi

import java.util.UUID

interface CommandRemainStatusPort {
    fun deleteRemainStatusByRemainOptionId(remainOptionId: UUID)
    fun saveRemainStatus(remainStatus: RemainStatus): RemainStatus
}