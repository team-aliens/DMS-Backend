package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service

@Service
class OutingService(
    commandOutingService: CommandOutingService,
) : CommandOutingService by commandOutingService
