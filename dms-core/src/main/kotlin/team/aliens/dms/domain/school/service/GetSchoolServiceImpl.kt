package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.util.UUID

@Service
class GetSchoolServiceImpl(
    private val querySchoolPort: QuerySchoolPort
) : GetSchoolService {

    override fun getAllSchools() =
        querySchoolPort.queryAllSchools()

    override fun getSchoolById(schoolId: UUID) =
        querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

    override fun getSchoolByCode(code: String) =
        querySchoolPort.querySchoolByCode(code) ?: throw SchoolCodeMismatchException

    override fun getAvailableFeaturesBySchoolId(schoolId: UUID) =
        querySchoolPort.queryAvailableFeaturesBySchoolId(schoolId) ?: throw FeatureNotFoundException
}
