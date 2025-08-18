package team.aliens.dms.domain.bug.spi

import team.aliens.dms.domain.bug.model.BugReport

interface CommandBugPort {

    fun saveBugReport(bugReport: BugReport): BugReport
}
