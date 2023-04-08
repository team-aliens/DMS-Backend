package team.aliens.dms.domain.file.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.file.exception.error.FileErrorCode

object FileIOInterruptedException : DmsException(
    FileErrorCode.IO_INTERRUPTED
)
