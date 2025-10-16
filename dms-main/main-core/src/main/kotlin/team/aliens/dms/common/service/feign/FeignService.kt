package team.aliens.dms.common.service.feign

import team.aliens.dms.common.annotation.Service

@Service
class FeignService(
    neisFeignService: NeisFeignService
) : NeisFeignService by neisFeignService
