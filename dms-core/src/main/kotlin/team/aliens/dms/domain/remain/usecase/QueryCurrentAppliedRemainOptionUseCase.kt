package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.CurrentAppliedRemainOptionResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryCurrentAppliedRemainOptionUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(): CurrentAppliedRemainOptionResponse {

        val user = userService.getCurrentUser()
        val appliedRemainOption = remainService.getAppliedRemainOptionByUserId(user.id)

        return CurrentAppliedRemainOptionResponse(appliedRemainOption.title)
    }
}
