package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val userService: UserService,
    private val pointService: PointService
) {

    fun execute(type: PointRequestType, pageData: PageData): QueryPointHistoryResponse {

        val student = userService.getCurrentStudent()
        val gcn = student.gcn
        val name = student.name
        val pointType = PointRequestType.toPointType(type)

        val pointHistories = pointService.queryPointHistoryByStudentGcnAndNameAndType(
            gcn = gcn,
            studentName = name,
            type = pointType,
            isCancel = false,
            pageData = pageData
        )

        val (bonusTotal, minusTotal) =
            pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name)

        return QueryPointHistoryResponse(
            totalPoint = pointService.getTotalPoint(type, bonusTotal, minusTotal),
            points = pointHistories
        )
    }
}
