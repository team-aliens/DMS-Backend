package team.aliens.dms.domain.bug.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.spi.SendBugPort

@Service
class SendBugServiceImpl(
    private val sendBugPort: SendBugPort
) : SendBugService {

    override fun sendBugReport(bugReport: BugReport) {
        sendBugPort.sendBugReport(bugReport)
    }
}
