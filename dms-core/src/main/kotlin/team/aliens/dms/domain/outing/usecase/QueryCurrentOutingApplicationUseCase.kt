package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.CurrentOutingApplicationResponse
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.student.service.StudentService


@ReadOnlyUseCase
class QueryCurrentOutingApplicationUseCase(
    private val outingService: OutingService,
    private val studentService: StudentService
) {

    fun execute(): CurrentOutingApplicationResponse {

        val student = studentService.getCurrentStudent()

        val outingApplication = outingService.getCurrentOutingApplication(student)
    }
}
