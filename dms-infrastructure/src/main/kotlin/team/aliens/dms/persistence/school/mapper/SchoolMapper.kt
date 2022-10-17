package team.aliens.dms.persistence.school.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Component
class SchoolMapper : GenericMapper<School, SchoolJpaEntity> {

    override fun toDomain(e: SchoolJpaEntity): School {
        return School(
            id = e.id,
            name = e.name,
            code = e.code,
            question = e.question,
            answer = e.answer,
            address = e.address,
            contractStartedAt = e.contractStartedAt,
            contractEndedAt = e.contractEndedAt
        )
    }

    override fun toEntity(d: School): SchoolJpaEntity {
        return SchoolJpaEntity(
            id = d.id,
            name = d.name,
            code = d.code,
            question = d.question,
            answer = d.answer,
            address = d.address,
            contractStartedAt = d.contractStartedAt,
            contractEndedAt = d.contractEndedAt
        )
    }
}