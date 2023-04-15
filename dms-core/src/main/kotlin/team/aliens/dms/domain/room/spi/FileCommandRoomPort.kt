package team.aliens.dms.domain.room.spi

import team.aliens.dms.domain.room.model.Room

interface FileCommandRoomPort {

    fun saveRooms(rooms: List<Room>): List<Room>
}