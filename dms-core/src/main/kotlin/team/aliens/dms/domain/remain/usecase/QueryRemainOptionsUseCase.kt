package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse
import team.aliens.dms.domain.remain.dto.QueryRemainOptionsResponse.RemainOptionElement
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val getUserService: GetUserService,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainStatusPort: QueryRemainStatusPort
) {

    fun execute(): QueryRemainOptionsResponse {

        val student = getUserService.getCurrentUser()

        val remainStatus = queryRemainStatusPort.queryRemainStatusById(student.id)

        val remainOptions = queryRemainOptionPort.queryAllRemainOptionsBySchoolId(student.schoolId)
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
