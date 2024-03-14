package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service

@Service
class PointService(
    getPointService: GetPointService,
    commandPointService: CommandPointService
) : GetPointService by getPointService,
    CommandPointService by commandPointService
