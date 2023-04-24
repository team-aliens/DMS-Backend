package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class ApplyRemainUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID) {

        val user = userService.getCurrentUser()

        val remainOption = remainService.getRemainOptionById(remainOptionId, user.schoolId)
        val remainAvailableTime = remainService.getRemainAvailableTimeBySchoolId(user.schoolId)
        remainAvailableTime.checkAvailable()

        remainService.saveRemainStatus(
            RemainStatus(
                id = user.id,
                remainOptionId = remainOption.id,
                createdAt = LocalDateTime.now()
            )
        )
    }
}
