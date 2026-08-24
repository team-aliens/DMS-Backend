package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationCanNotCancelException
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.service.StudentService

@UseCase
class CancelDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val studentService: StudentService
) {

    fun execute() {
        val studentId = studentService.getCurrentStudent().id
        val recentApplication = daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(studentId)

        if(recentApplication.status != Status.PENDING) {
            throw DaybreakStudyApplicationCanNotCancelException
        }

        daybreakService.deleteDaybreakStudyApplication(studentId)
    }
}