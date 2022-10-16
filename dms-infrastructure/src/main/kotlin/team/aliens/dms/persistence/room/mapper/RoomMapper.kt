package team.aliens.dms.persistence.room.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomEntity

@Mapper
interface RoomMapper : GenericMapper<Room, RoomEntity> {

    @Mapping(source = "id.roomNumber", target = "roomNumber")
    @Mapping(source = "schoolEntity.id", target = "schoolId")
    override fun toDomain(e: RoomEntity): Room

    @Mapping(source = "roomNumber", target = "id.roomNumber")
    @Mapping(source = "schoolId", target = "id.schoolId")
    override fun toEntity(d: Room): RoomEntity
}