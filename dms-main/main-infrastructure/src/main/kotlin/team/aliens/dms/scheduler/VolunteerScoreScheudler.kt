package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.usecase.ConvertVolunteerScoreToPointUseCase

@Component
class VolunteerScoreScheudler(
    private val convertVolunteerScoreToPointUseCase: ConvertVolunteerScoreToPointUseCase
) {
    @Scheduled(cron = "0 0 0 28 * *", zone = "Asia/Seoul")
    fun convertVolunteerScoreToPoint() = convertVolunteerScoreToPointUseCase
}