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
        val volunteerScores = volunteerService.getAllVolunteerScoresWithVO()

        val pointHistories = volunteerScores.map { volunteerScore ->
            PointHistory(
                studentName = volunteerScore.studentName,
                studentGcn = Student.processGcn(
                    volunteerScore.studentGrade,
                    volunteerScore.studentClassRoom,
                    volunteerScore.studentNumber
                ),
                bonusTotal = volunteerScore.bonusTotal + volunteerScore.assignScore,
                minusTotal = volunteerScore.minusTotal,
                isCancel = false,
                pointName = "봉사활동 상점",
                pointScore = volunteerScore.assignScore,
                pointType = PointType.BONUS,
                createdAt = LocalDateTime.now(),
                schoolId = volunteerScore.schoolId
            )
        }

        val studentIds = volunteerScores.map { it.studentId }
        pointService.saveAllPointHistories(pointHistories, studentIds)

        volunteerService.deleteAllVolunteerScores()
    }
}
