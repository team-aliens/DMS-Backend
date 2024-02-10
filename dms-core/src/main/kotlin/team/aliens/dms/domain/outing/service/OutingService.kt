package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service

@Service
class OutingService(
    commandOutingService: CommandOutingService,
    checkOutingService: CheckOutingService
) : CommandOutingService by commandOutingService,
    CheckOutingService by checkOutingService
