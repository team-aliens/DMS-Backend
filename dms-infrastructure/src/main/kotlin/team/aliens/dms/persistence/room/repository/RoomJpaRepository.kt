package team.aliens.dms.persistence.room.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId

@Repository
interface RoomJpaRepository : CrudRepository<RoomJpaEntity, RoomJpaEntityId> {
}