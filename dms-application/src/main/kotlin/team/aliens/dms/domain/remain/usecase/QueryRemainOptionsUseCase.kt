package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse.RemainOptionElement
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryRemainStatusPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val securityPort: RemainSecurityPort,
    private val queryUserPort: RemainQueryUserPort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainStatusPort: RemainQueryRemainStatusPort
) {

    fun execute(): QueryRemainOptionsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainOptions = queryRemainOptionPort.queryAllRemainOptionBySchoolId(currentUser.schoolId)
            .map {
                RemainOptionElement(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }

        val remainStatus = queryRemainStatusPort.queryRemainStatusById(currentUserId)

        return QueryRemainOptionsResponse(
            selectedOption = remainStatus?.let {
                queryRemainOptionPort.queryRemainOptionById(it.remainOptionId)?.title
            },
            remainOptions = remainOptions
        )
    }
}