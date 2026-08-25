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

        if (recentApplication.status != Status.PENDING) {
            throw DaybreakStudyApplicationCanNotCancelException
        }

        // 위 상태 확인과 삭제 사이에 선생님이 상태를 바꾸면 삭제 조건에 걸리지 않아 아무것도 지워지지 않는다
        if (!daybreakService.deleteDaybreakStudyApplication(studentId)) {
            throw DaybreakStudyApplicationCanNotCancelException
        }
    }
}
