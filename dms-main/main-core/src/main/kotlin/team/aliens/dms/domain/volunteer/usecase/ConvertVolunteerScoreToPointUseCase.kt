package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.time.LocalDateTime

@SchedulerUseCase
class ConvertVolunteerScoreToPointUseCase(
    private val volunteerService: VolunteerService,
    private val pointService: PointService
) {

    fun execute() {
        val volunteerScoreWithStudentVOs = volunteerService.getAllVolunteerScoresWithVO()

        val pointHistories = volunteerScoreWithStudentVOs.map { volunteerScoreWithStudentVO ->
            PointHistory(
                studentName = volunteerScoreWithStudentVO.studentName,
                studentGcn = Student.processGcn(
                    volunteerScoreWithStudentVO.studentGrade,
                    volunteerScoreWithStudentVO.studentClassRoom,
                    volunteerScoreWithStudentVO.studentNumber
                ),
                bonusTotal = volunteerScoreWithStudentVO.bonusTotal + volunteerScoreWithStudentVO.assignScore,
                minusTotal = volunteerScoreWithStudentVO.minusTotal,
                isCancel = false,
                pointName = "봉사활동 상점",
                pointScore = volunteerScoreWithStudentVO.assignScore,
                pointType = PointType.BONUS,
                createdAt = LocalDateTime.now(),
                schoolId = volunteerScoreWithStudentVO.schoolId
            )
        }

        val studentIds = volunteerScoreWithStudentVOs.map { it.studentId }
        pointService.saveAllPointHistories(pointHistories, studentIds)

        volunteerService.deleteAllVolunteerScores()
    }
}
