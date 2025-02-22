package team.aliens.dms.scheduler.error.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.scheduler.error.SchedulerErrorCode

object TaskSchedulerErrorException : DmsException(
    SchedulerErrorCode.TASK_SCHEDULER_ERROR
)
