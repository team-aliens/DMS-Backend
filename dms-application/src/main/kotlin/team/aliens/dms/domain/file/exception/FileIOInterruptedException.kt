package team.aliens.dms.domain.file.exception

import team.aliens.dms.domain.file.error.FileErrorCode
import team.aliens.dms.global.error.DmsException

object FileIOInterruptedException : DmsException(
    FileErrorCode.IO_INTERRUPTED
)