package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class ApplyRemainUseCase(
    private val userService: UserService,
    private val remainService: RemainService,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort
) {

    fun execute(remainOptionId: UUID) {

        val user = userService.getCurrentUser()

        val remainOption = remainService.getRemainOptionById(remainOptionId, user.schoolId)

        val remainAvailableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(user.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        if (!remainAvailableTime.isAvailable()) {
            throw RemainCanNotAppliedException
        }

        remainService.saveRemainStatus(
            RemainStatus(
                id = user.id,
                remainOptionId = remainOption.id,
                createdAt = LocalDateTime.now()
            )
        )
    }
}
