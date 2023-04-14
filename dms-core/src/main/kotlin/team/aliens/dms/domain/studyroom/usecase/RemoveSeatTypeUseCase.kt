package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.SeatTypeInUseException
import team.aliens.dms.domain.studyroom.exception.SeatTypeNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandSeatTypePort
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class RemoveSeatTypeUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val querySeatTypePort: QuerySeatTypePort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandSeatTypePort: CommandSeatTypePort
) {

    fun execute(seatTypeId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seatType = querySeatTypePort.querySeatTypeById(seatTypeId) ?: throw SeatTypeNotFoundException

        validateSameSchool(user.schoolId, seatType.schoolId)

        if (queryStudyRoomPort.existsSeatBySeatTypeId(seatTypeId)) {
            throw SeatTypeInUseException
        }

        commandSeatTypePort.deleteSeatType(seatType)
    }
}
