package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.response.GetCurrentOutingApplicationResponse
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class GetCurrentOutingApplicationUseCase(
    private val outingService: OutingService,
    private val studentService: StudentService
) {

    fun execute(): GetCurrentOutingApplicationResponse {
        val student = studentService.getCurrentStudent()

        outingService.checkQueryAble()
        val currentOutingApplicationVO = outingService.getCurrentOutingApplication(student.id)

        return GetCurrentOutingApplicationResponse.of(currentOutingApplicationVO, student.name)
    }
}
