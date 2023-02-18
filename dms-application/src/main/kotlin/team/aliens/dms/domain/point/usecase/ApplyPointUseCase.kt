package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.ApplyPointRequest
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.*
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.time.LocalDateTime

@UseCase
class ApplyPointUseCase(
    private val queryManagerPort: PointQueryManagerPort,
    private val securityPort: PointSecurityPort,
    private val queryPointPort: QueryPointPort,
    private val commandPointPort: CommandPointPort,
    private val queryStudentPort: PointQueryStudentPort
) {

    fun execute(applyPointRequest: ApplyPointRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId)?: throw ManagerNotFoundException

        val pointOption = queryPointPort.queryPointOptionByIdAndSchoolId(
            applyPointRequest.pointOptionId, manager.schoolId
        )?: throw PointOptionNotFoundException

        val studentList = applyPointRequest.studentIdList.map {
            val student = queryStudentPort.queryStudentById(it)?: throw StudentNotFoundException
            if (student.schoolId != manager.schoolId) throw SchoolMismatchException
            student
        }

        val pointList = studentList.map {
            var (bonusTotal, minusTotal) = queryPointPort.queryBonusAndMinusTotalPointByStudentGcnAndName(it.gcn, it.name)
            if(pointOption.type == PointType.BONUS)
                bonusTotal += pointOption.score
            else
                minusTotal += pointOption.score
            PointHistory(
                studentName = it.name,
                studentGcn = it.gcn,
                bonusTotal = bonusTotal,
                minusTotal = minusTotal,
                isCancel = false,
                pointName = pointOption.name,
                pointScore = pointOption.score,
                pointType = pointOption.type,
                createdAt = LocalDateTime.now(),
                schoolId = manager.schoolId
            )
        }

        commandPointPort.saveAllPointHistories(pointList)
    }
}