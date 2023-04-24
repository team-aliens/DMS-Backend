package team.aliens.dms.domain.room.service

import org.springframework.stereotype.Service

@Service
class RoomService(
    commandRoomService: CommandRoomService
) : CommandRoomService by commandRoomService
