package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class ReissueSchoolCodeUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val querySchoolPort: QuerySchoolPort,
    private val commandSchoolPort: CommandSchoolPort
) {

    fun execute(): String {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException
        val code = StringUtil.randomNumber(School.SCHOOL_CODE_SIZE)

        commandSchoolPort.saveSchool(
            school.copy(code = code)
        )

        return code
    }
}
