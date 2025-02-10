package team.aliens.dms.global.taskschduler

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.TaskSchedulerPort
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

@Component
class TaskSchedulerAdepter(
    private val taskScheduler: ThreadPoolTaskScheduler,

): TaskSchedulerPort {
    var scheduledTasks = ConcurrentHashMap<UUID,ScheduledFuture<*>>()


    override fun schduleTask(id: UUID, task: Runnable, time: LocalDateTime) {

        val InstantTime = time.atZone(ZoneId.systemDefault()).toInstant()
        val scheduledFuture = taskScheduler.schedule(task, InstantTime)

        scheduledTasks[id] = scheduledFuture
    }

    override fun cancelTask(id: UUID) {
        scheduledTasks[id]?.cancel(false)
        scheduledTasks.remove(id)
    }

}