package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.SeatTypeAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.spi.CommandSeatTypePort
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class CreateSeatTypeUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySeatTypePort: QuerySeatTypePort,
    private val commandSeatTypePort: CommandSeatTypePort
) {

    fun execute(name: String, color: String) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

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