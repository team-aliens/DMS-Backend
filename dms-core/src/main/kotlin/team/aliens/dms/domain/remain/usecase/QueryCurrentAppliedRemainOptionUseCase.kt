package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryCurrentAppliedRemainOptionResponse
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.exception.RemainStatusNotFound
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryCurrentAppliedRemainOptionUseCase(
    private val userService: UserService,
    private val queryRemainStatusPort: QueryRemainStatusPort,
    private val queryRemainOptionPort: QueryRemainOptionPort
) {

    fun execute(): QueryCurrentAppliedRemainOptionResponse {

        val user = userService.getCurrentUser()

        val remainStatus = queryRemainStatusPort.queryRemainStatusById(user.id)
            ?: throw RemainStatusNotFound

        val appliedRemainOption = queryRemainOptionPort.queryRemainOptionById(remainStatus.remainOptionId)
            ?: throw RemainOptionNotFoundException

        return QueryCurrentAppliedRemainOptionResponse(
            title = appliedRemainOption.title
        )
    }
}
