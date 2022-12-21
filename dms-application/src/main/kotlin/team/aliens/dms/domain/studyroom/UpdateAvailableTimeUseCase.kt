package team.aliens.dms.domain.studyroom

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime

@UseCase
class UpdateAvailableTimeUseCase(
    private val securityPort: StudyRoomAvailableTimeSecurityPort,
    private val queryAvailableTimePort: QueryStudyRoomAvailableTimePort,
    private val commandAvailableTimePort: CommandAvailableTimePort,
    private val queryUserPort: StudyRoomAvailableTimeQueryUserPort,
    private val querySchoolPort: StudyRoomAvailableTimeQuerySchoolPort
) {

    fun execute(startAt: LocalTime, endAt: LocalTime) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val availableTime = queryAvailableTimePort.queryStudyRoomAvailableTimeBySchoolId(school.id)
            ?: throw StudyRoomAvailableTimeNotFoundException

        commandAvailableTimePort.saveAvailableTime(
            availableTime.copy(
                startAt = startAt,
                endAt = endAt
            )
        )
    }
}