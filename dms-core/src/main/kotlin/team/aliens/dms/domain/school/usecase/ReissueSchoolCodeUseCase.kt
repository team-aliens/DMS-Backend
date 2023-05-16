package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.school.dto.ReissueSchoolCodeResponse
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class ReissueSchoolCodeUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(): ReissueSchoolCodeResponse {

        val user = userService.getCurrentUser()

        val school = schoolService.getSchoolById(user.schoolId)
        val code = StringUtil.randomNumber(School.SCHOOL_CODE_SIZE)

        schoolService.saveSchool(
            school.copy(code = code)
        )

        return ReissueSchoolCodeResponse(code = code)
    }
}
