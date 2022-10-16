package team.aliens.dms.persistence.point.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointHistoryEntity

@Mapper
interface PointHistoryMapper : GenericMapper<PointHistory, PointHistoryEntity> {


    @Mapping(source = "pointOptionEntity.id", target = "pointOptionId")
    @Mapping(source = "studentEntity.studentId", target = "studentId")
    override fun toDomain(e: PointHistoryEntity): PointHistory

    @Mapping(source = "pointOptionId", target = "pointOptionEntity.id")
    override fun toEntity(d: PointHistory): PointHistoryEntity
}