package team.aliens.dms.domain.bug.service

import team.aliens.dms.common.annotation.Service

@Service
class BugService(
    commandBugService: CommandBugService,
    sendBugService: SendBugService
) : CommandBugService by commandBugService,
    SendBugService by sendBugService
