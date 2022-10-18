package team.aliens.dms.persistence.school

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.SchoolPort
import team.aliens.dms.persistence.school.mapper.SchoolMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.*

@Component
class SchoolPersistenceAdapter(
    val schoolJpaRepository: SchoolJpaRepository,
    val schoolMapper: SchoolMapper
) : SchoolPort{

    override fun queryById(id: UUID): School? {
        return schoolMapper.toDomain(schoolJpaRepository.findByIdOrNull(id))
    }
}