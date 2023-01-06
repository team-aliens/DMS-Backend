package team.aliens.dms.thirdparty.parser.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object ExcelExtensionMismatchException : DmsException(
    GlobalErrorCode.EXTENSION_MISMATCH
)