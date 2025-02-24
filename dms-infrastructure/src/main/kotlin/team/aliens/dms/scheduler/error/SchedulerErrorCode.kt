package team.aliens.dms.scheduler.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SchedulerErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    TASK_SCHEDULER_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "Task Scheduler Error", 1);

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "SCHEDULER-$status-$sequence"
}
