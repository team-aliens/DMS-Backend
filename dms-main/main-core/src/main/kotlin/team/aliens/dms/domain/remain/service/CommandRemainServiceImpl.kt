package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.CommandRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import java.util.UUID

@Service
class CommandRemainServiceImpl(
    private val commandRemainAvailableTimePort: CommandRemainAvailableTimePort,
    private val commandRemainOptionPort: CommandRemainOptionPort,
    private val commandRemainStatusPort: CommandRemainStatusPort
) : CommandRemainService {

    override fun saveRemainAvailableTime(remainAvailableTime: RemainAvailableTime) =
        commandRemainAvailableTimePort.saveRemainAvailableTime(remainAvailableTime)

    override fun saveRemainOption(remainOption: RemainOption) =
        commandRemainOptionPort.saveRemainOption(remainOption)

    override fun deleteRemainOption(remainOption: RemainOption) {
        commandRemainStatusPort.deleteRemainStatusByRemainOptionId(remainOption.id)
        commandRemainOptionPort.deleteRemainOption(remainOption)
    }

    override fun saveRemainStatus(remainStatus: RemainStatus) =
        commandRemainStatusPort.saveRemainStatus(remainStatus)

    override fun deleteRemainStatusByStudentId(studentId: UUID) {
        commandRemainStatusPort.deleteByStudentId(studentId)
    }
}
