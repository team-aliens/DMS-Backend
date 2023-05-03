package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class ApplyRemainUseCase(
    private val studentService: StudentService,
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID) {

        val student = studentService.getCurrentStudent()

        val remainOption = remainService.getRemainOptionById(remainOptionId)
        remainService.getRemainAvailableTimeBySchoolId(student.schoolId)
            .apply { checkAvailable() }

        remainService.saveRemainStatus(
            RemainStatus(
                id = student.id,
                remainOptionId = remainOption.id,
                createdAt = LocalDateTime.now()
            )
        )
    }
}
