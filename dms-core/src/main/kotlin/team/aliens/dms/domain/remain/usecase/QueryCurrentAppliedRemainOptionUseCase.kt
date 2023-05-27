package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.CurrentAppliedRemainOptionResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryCurrentAppliedRemainOptionUseCase(
    private val studentService: StudentService,
    private val remainService: RemainService,
) {

    fun execute(): CurrentAppliedRemainOptionResponse {

        val student = studentService.getCurrentStudent()
        val appliedRemainOption = remainService.getAppliedRemainOptionByStudentId(student.id)

        return CurrentAppliedRemainOptionResponse(appliedRemainOption.title)
    }
}
