package team.aliens.dms.domain.bug.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.spi.CommandBugPort

@Service
class CommandBugServiceImpl(
    private val commandBugPort: CommandBugPort,
) : CommandBugService {

    override fun saveBugReport(bugReport: BugReport) =
        commandBugPort.saveBugReport(bugReport)
}
