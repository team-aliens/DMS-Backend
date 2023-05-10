package team.aliens.dms.persistence.security.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.common.model.SchoolSecret
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.persistence.EncryptableGenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.security.entity.SchoolSecretJpaEntity

@Component
class SchoolSecretMapper(
    private val schoolRepository: SchoolJpaRepository
) : EncryptableGenericMapper<SchoolSecret, SchoolSecretJpaEntity> {

    override fun toDomain(entity: SchoolSecretJpaEntity?): SchoolSecret? {
        return entity?.let {
            SchoolSecret(
                schoolId = it.schoolId,
                secretKey = it.secretKey
            )
        }
    }

    override fun toEntity(domain: SchoolSecret): SchoolSecretJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId) ?: throw SchoolNotFoundException
        return SchoolSecretJpaEntity(
            schoolId = domain.schoolId,
            school = school,
            secretKey = domain.secretKey
        )
    }
}
