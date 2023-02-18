package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.GivePointRequest
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.*
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.time.LocalDateTime

@UseCase
class GivePointUseCase(
    private val queryManagerPort: PointQueryManagerPort,
    private val securityPort: PointSecurityPort,
    private val queryPointPort: QueryPointPort,
    private val commandPointPort: CommandPointPort,
    private val queryStudentPort: PointQueryStudentPort
) {

    fun execute(givePointRequest: GivePointRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val pointOption = queryPointPort.queryPointOptionByIdAndSchoolId(
            givePointRequest.pointOptionId, manager.schoolId
        ) ?: throw PointOptionNotFoundException

        val pointList = queryStudentPort.queryStudentsWithPointHistory(givePointRequest.studentIdList)
            .map {
                if(it.schoolId != manager.schoolId)
                    throw SchoolMismatchException
                if(pointOption.type == PointType.BONUS)
                    it.bonusTotal += pointOption.score
                else
                    it.minusTotal += pointOption.score

                PointHistory(
                    studentName = it.name,
                    studentGcn = it.gcn,
                    bonusTotal = it.bonusTotal,
                    minusTotal = it.minusTotal,
                    isCancel = false,
                    pointName = pointOption.name,
                    pointScore = pointOption.score,
                    pointType = pointOption.type,
                    createdAt = LocalDateTime.now(),
                    schoolId = manager.schoolId
                )
            }
        if(pointList.size != givePointRequest.studentIdList.size)
            throw StudentNotFoundException

        commandPointPort.saveAllPointHistories(pointList)
    }
}