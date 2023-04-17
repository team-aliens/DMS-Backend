package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse.TypeElement
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class QuerySeatTypesUseCase(
    private val userService: UserService,
    private val querySeatTypePort: QuerySeatTypePort
) {

    fun execute(studyRoomId: UUID?): QuerySeatTypesResponse {

        val user = userService.getCurrentUser()

        val seatTypes = studyRoomId?.let {
            querySeatTypePort.queryAllSeatTypeByStudyRoomId(studyRoomId)
        } ?: querySeatTypePort.queryAllSeatTypeBySchoolId(user.schoolId)

        return QuerySeatTypesResponse(
            seatTypes.map {
                TypeElement(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }
        )
    }
}
