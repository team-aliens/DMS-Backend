package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(): QueryRemainOptionsResponse {

        val user = userService.getCurrentUser()

        val remainStatus = remainService.queryRemainStatusById(user.id)

        val remainOptions = remainService.getAllRemainOptionsBySchoolId(
            schoolId = user.schoolId,
            remainOptionId = remainStatus?.remainOptionId
        )

        return QueryRemainOptionsResponse(
            remainOptions = remainOptions
        )
    }
}
