package team.aliens.dms.persistence.security

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.common.model.SchoolSecret
import team.aliens.dms.common.spi.SchoolSecretPort
import team.aliens.dms.persistence.security.mapper.SchoolSecretMapper
import team.aliens.dms.persistence.security.repository.SchoolSecretJpaRepository
import java.util.UUID

@Component
class SchoolSecretPersistenceAdapter(
    private val schoolSecretRepository: SchoolSecretJpaRepository,
    private val schoolSecretMapper: SchoolSecretMapper
) : SchoolSecretPort {

    @Cacheable("schoolSecret")
    override fun querySchoolSecretBySchoolId(schoolId: UUID) =
        schoolSecretRepository.findByIdOrNull(schoolId)
            ?.let { schoolSecretMapper.toDomain(it) }

    override fun saveSchoolSecret(schoolSecret: SchoolSecret) {
        schoolSecretRepository.save(
            schoolSecretMapper.toEntity(schoolSecret)
        )
    }
}
