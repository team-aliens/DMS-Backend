package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse.RemainOptionElement
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(): QueryRemainOptionsResponse {

        val user = userService.getCurrentUser()

        val remainStatus = remainService.getRemainStatusById(user.id)

        val remainOptions = remainService.getAllRemainOptionsBySchoolId(user.schoolId)
            .map {
                RemainOptionElement(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isApplied = it.id == remainStatus?.remainOptionId
                )
            }

        return QueryRemainOptionsResponse(
            remainOptions = remainOptions
        )
    }
}
