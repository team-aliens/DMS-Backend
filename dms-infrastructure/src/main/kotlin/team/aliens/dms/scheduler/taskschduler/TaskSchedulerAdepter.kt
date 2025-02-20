package team.aliens.dms.scheduler.taskschduler

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.scheduler.error.exception.TaskSchedulingErrorException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledFuture

@Component
class TaskSchedulerAdepter(
    private val taskScheduler: ThreadPoolTaskScheduler,

) : TaskSchedulerPort {
    var scheduledTasks = ConcurrentHashMap<UUID, ScheduledFuture<*>>()

    override fun scheduleTask(taskId: UUID, task: Runnable, time: LocalDateTime) {

        val scheduledTime = time.atZone(ZoneId.systemDefault()).toInstant()
        try {
            val scheduledFuture = taskScheduler.schedule(task, scheduledTime)
            scheduledTasks[taskId] = scheduledFuture

            taskScheduler.schedule({ scheduledTasks.remove(taskId) }, scheduledTime)
        } catch (e: Exception) {
            throw TaskSchedulingErrorException
        }
    }

    override fun cancelTask(taskId: UUID) {
        scheduledTasks[taskId]?.cancel(false)
        scheduledTasks.remove(taskId)
    }
}
