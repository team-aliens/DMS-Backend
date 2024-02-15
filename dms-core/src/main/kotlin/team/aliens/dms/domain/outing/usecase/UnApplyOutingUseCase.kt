package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.IsNotWriterException
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.user.service.UserService
import java.util.*

@UseCase
class UnApplyOutingUseCase(
    private val userService: UserService,
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID) {
        val user = userService.getCurrentUser()
        val outing = outingService.getOutingById(outingApplicationId)

        if (outing.studentId != user.id) {
            throw  IsNotWriterException
        }

        outingService.deleteOutingApplication(outing)
    }
}