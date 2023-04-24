package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.util.UUID

@Service
class GetSchoolServiceImpl(
    private val querySchoolPort: QuerySchoolPort
) : GetSchoolService {

    override fun getSchoolById(schoolId: UUID) =
        querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

    override fun queryAllSchools(): List<School> {
        return querySchoolPort.queryAllSchools()
    }

    override fun getAvailableFeaturesBySchoolId(schoolId: UUID): AvailableFeature {
        return querySchoolPort.queryAvailableFeaturesBySchoolId(schoolId)
            ?: throw FeatureNotFoundException
    }
}
