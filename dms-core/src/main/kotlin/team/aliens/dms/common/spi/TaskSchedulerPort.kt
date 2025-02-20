package team.aliens.dms.common.spi

import java.time.LocalDateTime
import java.util.UUID

interface TaskSchedulerPort {

    fun scheduleTask(taskId: UUID, task: Runnable, time: LocalDateTime)

    fun cancelTask(taskId: UUID)
}
