package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse.RemainOptionElement
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainStatusPort: QueryRemainStatusPort
) {

    fun execute(): QueryRemainOptionsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainStatus = queryRemainStatusPort.queryRemainStatusById(currentUserId)

        val remainOptions = queryRemainOptionPort.queryAllRemainOptionsBySchoolId(currentUser.schoolId)
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
