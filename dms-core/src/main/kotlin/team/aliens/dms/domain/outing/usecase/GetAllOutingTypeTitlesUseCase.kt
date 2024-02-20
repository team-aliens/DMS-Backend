package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.GetAllOutingTypeTitlesResponse
import team.aliens.dms.domain.outing.service.GetOutingService

@ReadOnlyUseCase
class GetAllOutingTypeTitlesUseCase(
    private val outingService: GetOutingService,
    private val securityService: SecurityService
) {

    fun execute(keyword: String?): GetAllOutingTypeTitlesResponse {
        val schoolId = securityService.getCurrentSchoolId()
        val titles = outingService.getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId, keyword)

        return GetAllOutingTypeTitlesResponse(titles)
    }
}
