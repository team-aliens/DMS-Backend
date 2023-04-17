package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryPointOptionsUseCase(
    private val userService: UserService,
    private val queryPointOptionPort: QueryPointOptionPort
) {

    fun execute(keyword: String?): QueryPointOptionsResponse {

        val user = userService.getCurrentUser()

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
