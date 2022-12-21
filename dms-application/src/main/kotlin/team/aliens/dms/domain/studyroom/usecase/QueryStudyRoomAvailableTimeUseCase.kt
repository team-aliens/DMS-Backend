package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.studyroom.dto.QueryStudyRoomAvailableTimeResponse
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryStudyRoomAvailableTimeUseCase(
    private val securityPort: StudyRoomAvailableTimeSecurityPort,
    private val queryStudyRoomAvailableTimePort: QueryStudyRoomAvailableTimePort,
    private val queryUserPort: StudyRoomAvailableTimeQueryUserPort,
    private val querySchoolPort: StudyRoomAvailableTimeQuerySchoolPort
) {

    fun execute(): QueryStudyRoomAvailableTimeResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val studyRoomAvailableTime = queryStudyRoomAvailableTimePort
            .queryStudyRoomAvailableTimeBySchoolId(school.id) ?: throw StudyRoomAvailableTimeNotFoundException

        return QueryStudyRoomAvailableTimeResponse(
            startAt = studyRoomAvailableTime.startAt,
            endAt = studyRoomAvailableTime.endAt
        )
    }
}