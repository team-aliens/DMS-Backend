package team.aliens.dms.persistence.point.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointOptionJpaEntity

@Component
class PointOptionMapper : GenericMapper<PointOption, PointOptionJpaEntity> {

    override fun toDomain(entity: PointOptionJpaEntity?): PointOption? {
        return PointOption(
            id = entity!!.id,
            name = entity.name,
            score = entity.score
        )
    }

    override fun toEntity(domain: PointOption): PointOptionJpaEntity {
        return PointOptionJpaEntity(
            id = domain.id,
            name = domain.name,
            score = domain.score
        )
    }
}