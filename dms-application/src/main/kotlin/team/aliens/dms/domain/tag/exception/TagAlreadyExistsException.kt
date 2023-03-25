package team.aliens.dms.domain.tag.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.tag.error.TagErrorCode

object TagAlreadyExistsException : DmsException(TagErrorCode.TAG_ALREADY_EXISTS)
