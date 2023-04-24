package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service

@Service
class RemainService(
    getRemainService: GetRemainService,
    commandRemainService: CommandRemainService
) : GetRemainService by getRemainService,
    CommandRemainService by commandRemainService
