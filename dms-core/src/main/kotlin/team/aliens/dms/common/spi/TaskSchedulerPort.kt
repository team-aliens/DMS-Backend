package team.aliens.dms.common.spi

import java.time.LocalDateTime

interface TaskSchedulerPort {
    fun schduleTask(task: Runnable, time: LocalDateTime)
}