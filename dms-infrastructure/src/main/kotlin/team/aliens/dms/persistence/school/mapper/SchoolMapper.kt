package team.aliens.dms.persistence.school.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Component
class SchoolMapper : GenericMapper<School, SchoolJpaEntity> {

    override fun toDomain(entity: SchoolJpaEntity?): School? {
        return School(
            id = entity!!.id,
            name = entity.name,
            code = entity.code,
            question = entity.question,
            answer = entity.answer,
            address = entity.address,
            contractStartedAt = entity.contractStartedAt,
            contractEndedAt = entity.contractEndedAt
        )
    }

    override fun toEntity(domain: School): SchoolJpaEntity {
        return SchoolJpaEntity(
            id = domain.id!!,
            name = domain.name,
            code = domain.code,
            question = domain.question,
            answer = domain.answer,
            address = domain.address,
            contractStartedAt = domain.contractStartedAt,
            contractEndedAt = domain.contractEndedAt
        )
    }
}