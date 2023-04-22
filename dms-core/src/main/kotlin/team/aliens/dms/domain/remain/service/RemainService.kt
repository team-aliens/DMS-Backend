package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service

@Service
class RemainService(
    checkRemainService: CheckRemainService,
    getRemainService: GetRemainService,
    commandRemainService: CommandRemainService
) : CheckRemainService by checkRemainService,
    GetRemainService by getRemainService,
    CommandRemainService by commandRemainService
