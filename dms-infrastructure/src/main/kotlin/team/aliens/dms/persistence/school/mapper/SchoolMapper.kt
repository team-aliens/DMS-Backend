package team.aliens.dms.persistence.school.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Component
class SchoolMapper : GenericMapper<School, SchoolJpaEntity> {

    override fun toDomain(entity: SchoolJpaEntity?): School? {
        return entity?.let {
            School(
                id = it.id,
                name = it.name,
                code = it.code,
                question = it.question,
                answer = it.answer,
                address = it.address,
                contractStartedAt = it.contractStartedAt,
                contractEndedAt = it.contractEndedAt
            )
        }
    }

    override fun toEntity(domain: School): SchoolJpaEntity {
        return SchoolJpaEntity(
            id = domain.id,
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