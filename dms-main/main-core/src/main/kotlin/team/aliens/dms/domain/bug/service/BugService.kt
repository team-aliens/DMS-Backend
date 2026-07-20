package team.aliens.dms.domain.bug.service

import team.aliens.dms.common.annotation.Service

@Service
class BugService(
    commandBugService: CommandBugService
) : CommandBugService by commandBugService
