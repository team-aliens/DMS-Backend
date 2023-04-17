package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.SeatTypeAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.spi.CommandSeatTypePort
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CreateSeatTypeUseCase(
    private val userService: UserService,
    private val querySeatTypePort: QuerySeatTypePort,
    private val commandSeatTypePort: CommandSeatTypePort
) {

    fun execute(name: String, color: String) {

        val user = userService.getCurrentUser()

        if (querySeatTypePort.existsSeatTypeByName(name)) {
            throw SeatTypeAlreadyExistsException
        }

        val seatType = SeatType(
            schoolId = user.schoolId,
            name = name,
            color = color
        )

        commandSeatTypePort.saveSeatType(seatType)
    }
}
