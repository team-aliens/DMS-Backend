package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort

@Service
class GetSchoolServiceImpl(
    private val querySchoolPort: QuerySchoolPort
) : GetSchoolService {

    override fun queryAllSchools(): List<School> {
        return querySchoolPort.queryAllSchools()
    }
}
