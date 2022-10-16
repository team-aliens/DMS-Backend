package team.aliens.dms.persistence.point.mapper

import org.mapstruct.Mapper
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointOptionEntity

@Mapper
interface PointOptionMapper : GenericMapper<PointOption, PointOptionEntity> {

    override fun toDomain(e: PointOptionEntity): PointOption

    override fun toEntity(d: PointOption): PointOptionEntity
}