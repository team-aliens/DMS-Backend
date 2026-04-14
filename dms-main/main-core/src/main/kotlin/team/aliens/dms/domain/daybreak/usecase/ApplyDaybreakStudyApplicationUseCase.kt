package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.ApplyDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.service.StudentService

@UseCase
class ApplyDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService,
    private val studentService: StudentService
) {

    fun execute(request: ApplyDaybreakStudyApplicationRequest) {

        val student = studentService.getCurrentStudent()
        val schoolId = securityService.getCurrentSchoolId()

        daybreakService.checkDaybreakStudyApplicationByStudentId(student.id)

        val studyType = daybreakService.getDaybreakStudyTypeById(request.typeId)

        daybreakService.saveDaybreakStudyApplication(
            DaybreakStudyApplication(
                studentId = student.id,
                studyTypeId = studyType.id,
                startDate = request.startDate,
                endDate = request.endDate,
                reason = request.reason,
                status = Status.PENDING,
                teacherId = request.teacherId,
                schoolId = schoolId,
            )
        )
    }
}
