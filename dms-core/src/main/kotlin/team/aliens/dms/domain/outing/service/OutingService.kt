package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service

@Service
class OutingService(
    commandOutingService: CommandOutingService,
    checkOutingService: CheckOutingService,
    getOutingService: GetOutingService
) : CommandOutingService by commandOutingService,
    CheckOutingService by checkOutingService,
    GetOutingService by getOutingService
