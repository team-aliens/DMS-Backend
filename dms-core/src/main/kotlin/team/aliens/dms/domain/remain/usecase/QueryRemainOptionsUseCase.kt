package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.RemainOptionsResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(): RemainOptionsResponse {

        val user = userService.getCurrentUser()

        val remainStatus = remainService.getRemainStatusById(user.id)

        val remainOptions = remainService.getAllRemainOptionsBySchoolId(user.schoolId)

        return RemainOptionsResponse.of(
            remainOptions = remainOptions,
            remainOptionId = remainStatus?.remainOptionId
        )
    }
}
