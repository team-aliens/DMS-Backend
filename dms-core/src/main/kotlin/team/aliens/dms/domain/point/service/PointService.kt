package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service

@Service
class PointService(
    checkPointService: CheckPointService,
    getPointService: GetPointService,
    commandPointService: CommandPointService
) : GetPointService by getPointService,
    CheckPointService by checkPointService,
    CommandPointService by commandPointService