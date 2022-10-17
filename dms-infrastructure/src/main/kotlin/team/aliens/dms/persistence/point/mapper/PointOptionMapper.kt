package team.aliens.dms.persistence.point.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointOptionJpaEntity

@Component
class PointOptionMapper : GenericMapper<PointOption, PointOptionJpaEntity> {

    override fun toDomain(e: PointOptionJpaEntity): PointOption {
        return PointOption(
            id = e.id,
            name = e.name,
            score = e.score
        )
    }

    override fun toEntity(d: PointOption): PointOptionJpaEntity {
        return PointOptionJpaEntity(
            id = d.id,
            name = d.name,
            score = d.score
        )
    }
}