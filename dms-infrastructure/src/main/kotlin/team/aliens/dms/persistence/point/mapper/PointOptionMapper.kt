package team.aliens.dms.persistence.point.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointOptionJpaEntity

@Component
class PointOptionMapper : GenericMapper<PointOption, PointOptionJpaEntity> {

    override fun toDomain(entity: PointOptionJpaEntity?): PointOption? {
        return entity?.let {
            PointOption(
                id = it.id,
                name = it.name,
                score = it.score
            )
        }
    }

    override fun toEntity(domain: PointOption): PointOptionJpaEntity {
        return PointOptionJpaEntity(
            id = domain.id,
            name = domain.name,
            score = domain.score
        )
    }
}