package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryAvailableTimeUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryAvailableTimePort: QueryAvailableTimePort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySchoolPort: StudyRoomQuerySchoolPort
) {

    fun execute(): QueryAvailableTimeResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val availableTime = queryAvailableTimePort
            .queryAvailableTimeBySchoolId(school.id) ?: throw AvailableTimeNotFoundException

        return QueryAvailableTimeResponse(
            startAt = availableTime.startAt,
            endAt = availableTime.endAt
        )
    }
}