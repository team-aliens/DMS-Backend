package team.aliens.dms.persistence.school

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.spi.SchoolPort
import team.aliens.dms.persistence.school.mapper.SchoolMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.*

@Component
class SchoolPersistenceAdapter(
    val schoolJpaRepository: SchoolJpaRepository,
    val schoolMapper: SchoolMapper
) : SchoolPort {

    override fun querySchoolById(id: UUID) = schoolMapper.toDomain(
        schoolJpaRepository.findByIdOrNull(id)
    )
}