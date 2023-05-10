package team.aliens.dms.persistence.school

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.SchoolPort
import team.aliens.dms.persistence.school.mapper.AvailableFeatureMapper
import team.aliens.dms.persistence.school.mapper.SchoolMapper
import team.aliens.dms.persistence.school.repository.AvailableFeatureJpaRepository
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.UUID

@Component
class SchoolPersistenceAdapter(
    private val schoolMapper: SchoolMapper,
    private val availableFeatureMapper: AvailableFeatureMapper,
    private val schoolRepository: SchoolJpaRepository,
    private val availableFeatureRepository: AvailableFeatureJpaRepository
) : SchoolPort {

    override fun queryAllSchools() = schoolRepository.findAll().map {
        schoolMapper.toDomain(it)!!
    }

    override fun saveSchool(school: School) = schoolMapper.toDomain(
        schoolRepository.save(
            schoolMapper.toEntity(school)
        )
    )!!

    override fun saveAvailableFeature(availableFeature: AvailableFeature) = availableFeatureMapper.toDomain(
        availableFeatureRepository.save(
            availableFeatureMapper.toEntity(availableFeature)
        )
    )!!

    override fun queryAvailableFeaturesBySchoolId(schoolId: UUID) = availableFeatureMapper.toDomain(
        availableFeatureRepository.findByIdOrNull(schoolId)
    )

    override fun querySchoolById(schoolId: UUID) = schoolMapper.toDomain(
        schoolRepository.findByIdOrNull(schoolId)
    )

    override fun querySchoolByCode(code: String) = schoolMapper.toDomain(
        schoolRepository.findByCode(code)
    )
}
