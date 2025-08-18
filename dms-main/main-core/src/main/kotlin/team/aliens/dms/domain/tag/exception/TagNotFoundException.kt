package team.aliens.dms.domain.tag.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.tag.exception.error.TagErrorCode

object TagNotFoundException : DmsException(
    TagErrorCode.TAG_NOT_FOUND
)
