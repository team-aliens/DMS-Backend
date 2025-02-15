package team.aliens.dms.common.spi

import java.time.LocalDateTime
import java.util.UUID

interface TaskSchedulerPort {

    fun scheduleTask(id: UUID, task: Runnable, time: LocalDateTime)

    fun cancelTask(id: UUID)
}
