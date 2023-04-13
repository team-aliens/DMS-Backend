package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.manager.dto.ManagerMyPageResponse
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort

@ReadOnlyUseCase
class ManagerMyPageUseCase(
    private val securityPort: SecurityPort,
    private val querySchoolPort: QuerySchoolPort,
    private val queryManagerPort: QueryManagerPort
) {

    fun execute(): ManagerMyPageResponse {
        val currentManagerId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentManagerId) ?: throw ManagerNotFoundException

        val school = querySchoolPort.querySchoolById(manager.schoolId) ?: throw SchoolNotFoundException

        return ManagerMyPageResponse(
            schoolId = school.id,
            schoolName = school.name,
            code = school.code,
            question = school.question,
            answer = school.answer
        )
    }
}
