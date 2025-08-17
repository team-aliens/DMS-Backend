package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service

@Service
class TagService(
    getTagService: GetTagService,
    commandTagService: CommandTagService,
    checkTagService: CheckTagService
) : GetTagService by getTagService,
    CommandTagService by commandTagService,
    CheckTagService by checkTagService
