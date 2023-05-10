package team.aliens.dms.domain.room.service

import team.aliens.dms.common.annotation.Service

@Service
class RoomService(
    commandRoomService: CommandRoomService
) : CommandRoomService by commandRoomService
