package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.dto.CreateOutingApplicationRequest
import team.aliens.dms.domain.outing.dto.CreateOutingApplicationResponse
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@UseCase
class CreateOutingApplicationUseCase(
    private val outingService: OutingService,
    private val userService: UserService
) {

    fun execute(request: CreateOutingApplicationRequest): CreateOutingApplicationResponse {
        val user = userService.getCurrentUser()

        val outing = outingService.saveOutingApplication(
            OutingApplication(
                studentId = user.id,
                createdAt = LocalDateTime.now(),
                outAt = request.outAt,
                outingTime = request.outingTime,
                arrivalTime = request.arrivalTime,
                status = OutingStatus.REQUESTED,
                reason = request.reason,
                destination = request.destination,
                outingTypeTitle = request.outingTypeId.title,
                schoolId = user.schoolId

            )
        )

        return CreateOutingApplicationResponse(outing.id)
    }
}
