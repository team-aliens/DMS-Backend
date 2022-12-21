package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.studyroom.exception.AvailableTimeNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime

@UseCase
class UpdateAvailableTimeUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryAvailableTimePort: QueryAvailableTimePort,
    private val commandAvailableTimePort: CommandAvailableTimePort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySchoolPort: StudyRoomQuerySchoolPort
) {

    fun execute(startAt: LocalTime, endAt: LocalTime) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val availableTime = queryAvailableTimePort.queryAvailableTimeBySchoolId(school.id)
            ?: throw AvailableTimeNotFoundException

        commandAvailableTimePort.saveAvailableTime(
            availableTime.copy(
                startAt = startAt,
                endAt = endAt
            )
        )
    }
}