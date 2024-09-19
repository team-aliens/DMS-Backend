package team.aliens.dms.domain.bug.service

import team.aliens.dms.domain.bug.model.BugReport

interface SendBugService {

    fun sendBugReport(bugReport: BugReport)
}
