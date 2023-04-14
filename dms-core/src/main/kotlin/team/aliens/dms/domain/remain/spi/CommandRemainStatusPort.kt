package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainStatus
import java.util.UUID

interface CommandRemainStatusPort {

    fun deleteRemainStatusByRemainOptionId(remainOptionId: UUID)

    fun saveRemainStatus(remainStatus: RemainStatus): RemainStatus

    fun deleteByStudentId(studentId: UUID)
}
