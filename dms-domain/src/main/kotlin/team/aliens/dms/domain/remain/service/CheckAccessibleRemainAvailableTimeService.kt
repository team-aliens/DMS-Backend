package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import java.time.LocalDateTime

@DomainService
class CheckAccessibleRemainAvailableTimeService : CheckAccessibleRemainAvailableTime {

    override fun execute(availableTime: RemainAvailableTime): Boolean {
        val currentDateTime = LocalDateTime.now()
        val dayOfWeek = currentDateTime.dayOfWeek.value
        val now = currentDateTime.toLocalTime()

        if (dayOfWeek < availableTime.startDayOfWeek.value || dayOfWeek > availableTime.endDayOfWeek.value) {
            return false
        }

        if (now < availableTime.startTime || now > availableTime.endTime) {
            return false
        }

        return true
    }
}