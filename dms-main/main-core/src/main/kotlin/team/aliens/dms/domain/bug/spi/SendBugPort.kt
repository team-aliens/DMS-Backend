package team.aliens.dms.domain.bug.spi

import team.aliens.dms.domain.bug.model.BugReport

interface SendBugPort {

    fun sendBugReport(bugReport: BugReport)
}
