package team.aliens.dms.persistence.room.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.room.entity.RoomEntity
import team.aliens.dms.persistence.room.entity.RoomEntityId

@Repository
interface RoomRepository : CrudRepository<RoomEntity, RoomEntityId> {
}