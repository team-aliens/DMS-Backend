package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.SeatTypeInUseException
import team.aliens.dms.domain.studyroom.exception.SeatTypeNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandSeatTypePort
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveSeatTypeUseCase(
    private val userService: UserService,
    private val querySeatTypePort: QuerySeatTypePort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandSeatTypePort: CommandSeatTypePort
) {

    fun execute(seatTypeId: UUID) {

        val user = userService.getCurrentUser()

        val seatType = querySeatTypePort.querySeatTypeById(seatTypeId) ?: throw SeatTypeNotFoundException

        validateSameSchool(user.schoolId, seatType.schoolId)

        if (queryStudyRoomPort.existsSeatBySeatTypeId(seatTypeId)) {
            throw SeatTypeInUseException
        }

        commandSeatTypePort.deleteSeatType(seatType)
    }
}
