package team.aliens.dms.persistence.school

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.spi.SchoolPort
import team.aliens.dms.persistence.school.mapper.SchoolMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.*

@Component
class SchoolPersistenceAdapter(
    private val schoolMapper: SchoolMapper,
    private val schoolRepository: SchoolJpaRepository
) : SchoolPort {

    override fun queryAllSchool() = schoolRepository.findAll().map {
        schoolMapper.toDomain(it)!!
    }

    override fun querySchoolById(id: UUID) = schoolMapper.toDomain(
        schoolRepository.findByIdOrNull(id)
    )
}