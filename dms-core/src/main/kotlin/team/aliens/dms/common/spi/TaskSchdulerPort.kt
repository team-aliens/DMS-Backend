package team.aliens.dms.common.spi

import java.time.LocalDateTime
import java.util.Date

interface TaskSchdulerPort {
    fun schduleTask(task: Runnable, time: LocalDateTime)
}