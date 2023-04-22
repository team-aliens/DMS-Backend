package team.aliens.dms.domain.room.spi

import team.aliens.dms.domain.room.model.Room

interface CommandRoomPort {

    fun saveRooms(rooms: List<Room>): List<Room>
}
