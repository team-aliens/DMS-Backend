package team.aliens.dms.domain.teacher.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.teacher.dto.response.TeacherResponse
import team.aliens.dms.domain.teacher.dto.response.TeachersResponse
import team.aliens.dms.domain.teacher.service.TeacherService

@UseCase
class QueryGeneralTeacherUseCase(
    private val teacherService: TeacherService,
    private val securityService: SecurityService
) {

    fun execute(): TeachersResponse {

        val schoolId = securityService.getCurrentSchoolId()

        val teacherList = teacherService.getGeneralTeachersBySchoolId(schoolId)

        return TeachersResponse(
            teachers = teacherList.map { teacher ->
                TeacherResponse(
                    id = teacher.id,
                    name = teacher.name
                )
            }
        )
    }
}
