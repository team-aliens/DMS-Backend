package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort

@SchedulerUseCase
class ResetAllStudyRoomsUseCase(
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute() {
        val studyRooms = queryStudyRoomPort.queryAllStudyRooms().map {
            it.copy(
                availableHeadcount = 0
            )
        }

        val seats = queryStudyRoomPort.queryAllSeatsBySeatStatus(SeatStatus.IN_USE).map {
            it.copy(
                studentId = null,
                status = SeatStatus.AVAILABLE
            )
        }

        commandStudyRoomPort.saveAllStudyRooms(studyRooms)
        commandStudyRoomPort.saveAllSeat(seats)
    }
}
