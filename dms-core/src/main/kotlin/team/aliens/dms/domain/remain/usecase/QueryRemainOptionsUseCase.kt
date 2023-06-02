package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.RemainOptionsResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryRemainOptionsUseCase(
    private val studentService: StudentService,
    private val remainService: RemainService
) {

    fun execute(): RemainOptionsResponse {

        val student = studentService.getCurrentStudent()

        val remainStatus = remainService.getRemainStatusById(student.id)

        val remainOptions = remainService.getAllRemainOptionsBySchoolId(student.schoolId)

        return RemainOptionsResponse.of(
            remainOptions = remainOptions,
            remainOptionId = remainStatus?.remainOptionId
        )
    }
}
