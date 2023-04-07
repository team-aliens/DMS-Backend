package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort

@ReadOnlyUseCase
class QueryPointOptionsUseCase(
    private val securityPort: PointSecurityPort,
    private val queryManagerPort: PointQueryManagerPort,
    private val queryPointOptionPort: QueryPointOptionPort
) {

    fun execute(keyword: String?): QueryPointOptionsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val pointOptions = queryPointOptionPort.queryPointOptionsBySchoolIdAndKeyword(manager.schoolId, keyword)
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
