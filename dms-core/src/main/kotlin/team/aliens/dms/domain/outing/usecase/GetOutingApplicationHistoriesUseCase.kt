package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.response.OutingApplicationHistoriesResponse
import team.aliens.dms.domain.outing.service.OutingService
import java.time.LocalDate

@ReadOnlyUseCase
class GetOutingApplicationHistoriesUseCase(
    private val outingService: OutingService,
    private val securityService: SecurityService
) {

    fun execute(studentName: String?, date: LocalDate): OutingApplicationHistoriesResponse {
        val schoolId = securityService.getCurrentSchoolId()

        val outings = outingService.getOutingHistoriesByStudentNameAndDate(
            studentName = studentName,
            schoolId = schoolId,
            date = date
        )

        return OutingApplicationHistoriesResponse(outings)
    }
}
