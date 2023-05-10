package team.aliens.dms.domain.school.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.school.dto.CreateSchoolRequest
import team.aliens.dms.domain.school.service.SchoolService

@UseCase
class CreateSchoolUseCase(
    private val schoolService: SchoolService,
    private val securityService: SecurityService
) {

    fun execute(request: CreateSchoolRequest) {
        val school = schoolService.saveSchool(
            request.toSchool()
        )
        schoolService.saveAvailableFeature(request.toAvailableFeature(school.id))
        securityService.createSchoolSecretBySchoolId(school.id)
    }
}