package team.aliens.dms.persistence.school

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.spi.SchoolPort
import team.aliens.dms.persistence.school.mapper.SchoolMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.UUID

@Component
class SchoolPersistenceAdapter(
    private val schoolMapper: SchoolMapper,
    private val schoolRepository: SchoolJpaRepository
) : SchoolPort {

    override fun queryAllSchools() = schoolRepository.findAll().map {
        schoolMapper.toDomain(it)!!
    }

    override fun querySchoolById(schoolId: UUID) = schoolMapper.toDomain(
        schoolRepository.findByIdOrNull(schoolId)
    )

    override fun querySchoolByCode(code: String) = schoolMapper.toDomain(
        schoolRepository.findByCode(code)
    )
}