package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.spi.CommandAvailableTimePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.time.LocalTime

@UseCase
class UpdateAvailableTimeUseCase(
    private val securityPort: SecurityPort,
    private val commandAvailableTimePort: CommandAvailableTimePort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(startAt: LocalTime, endAt: LocalTime) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        commandAvailableTimePort.saveAvailableTime(
            AvailableTime(
                schoolId = user.schoolId,
                startAt = startAt,
                endAt = endAt
            )
        )
    }
}
