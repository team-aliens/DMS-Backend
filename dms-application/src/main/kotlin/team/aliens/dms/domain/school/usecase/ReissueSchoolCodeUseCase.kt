package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class ReissueSchoolCodeUseCase(
    private val securityPort: SchoolSecurityPort,
    private val queryUserPort: SchoolQueryUserPort,
    private val querySchoolPort: QuerySchoolPort,
    private val commandSchoolPort: CommandSchoolPort
) {

    fun execute(): String {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException
        val updateSchool = commandSchoolPort.saveSchool(
            school.copy(code = StringUtil.randomNumber(8))
        )

        return updateSchool.code
    }
}