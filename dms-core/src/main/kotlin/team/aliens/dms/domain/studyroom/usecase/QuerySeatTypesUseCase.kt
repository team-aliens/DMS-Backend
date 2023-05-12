package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.SeatTypesResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QuerySeatTypesUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID?): SeatTypesResponse {

        val user = userService.getCurrentUser()
        val seatTypes = studyRoomService.getSeatTypes(user.schoolId, studyRoomId)

        return SeatTypesResponse.of(seatTypes)
    }
}
