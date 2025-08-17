package team.aliens.dms.domain.bug.service

import team.aliens.dms.domain.bug.model.BugReport

interface CommandBugService {

    fun saveBugReport(bugReport: BugReport): BugReport
}
