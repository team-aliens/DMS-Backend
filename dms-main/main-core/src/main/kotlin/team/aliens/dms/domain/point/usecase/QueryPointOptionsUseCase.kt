package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.PointOptionsResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryPointOptionsUseCase(
    private val userService: UserService,
    private val pointService: PointService
) {

    fun execute(keyword: String?): PointOptionsResponse {

        val user = userService.getCurrentUser()

        val pointOptions = pointService.queryPointOptionsBySchoolIdAndKeyword(user.schoolId, keyword)

        return PointOptionsResponse.of(pointOptions)
    }
}
