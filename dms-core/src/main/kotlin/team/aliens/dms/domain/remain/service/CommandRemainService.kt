package team.aliens.dms.domain.remain.service

import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus
import java.util.UUID

interface CommandRemainService {

    fun saveRemainAvailableTime(remainAvailableTime: RemainAvailableTime): RemainAvailableTime

    fun saveRemainOption(remainOption: RemainOption): RemainOption

    fun deleteRemainOption(remainOption: RemainOption)

    fun saveRemainStatus(remainStatus: RemainStatus): RemainStatus

    fun deleteRemainStatusByStudentId(studentId: UUID)
}
