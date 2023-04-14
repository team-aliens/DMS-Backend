package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryPointOptionsUseCase(
    private val getUserService: GetUserService,
    private val queryPointOptionPort: QueryPointOptionPort
) {

    fun execute(keyword: String?): QueryPointOptionsResponse {

        val user = getUserService.getCurrentUser()

        val pointOptions = queryPointOptionPort.queryPointOptionsBySchoolIdAndKeyword(user.schoolId, keyword)
            .map {
                QueryPointOptionsResponse.PointOptionResponse(
                    pointOptionId = it.id,
                    name = it.name,
                    type = it.type,
                    score = it.score
                )
            }

        return QueryPointOptionsResponse(pointOptions)
    }
}
