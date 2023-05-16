package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointHistoryResponse
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(type: PointRequestType, pageData: PageData): PointHistoryResponse {

        val student = studentService.getCurrentStudent()
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

        return PointHistoryResponse(
            totalPoint = PointHistory.getTotalPoint(type, bonusTotal, minusTotal),
            pointHistories = pointHistories
        )
    }
}
