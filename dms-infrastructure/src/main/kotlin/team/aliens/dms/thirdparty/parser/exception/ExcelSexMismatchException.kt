package team.aliens.dms.thirdparty.parser.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object ExcelSexMismatchException : DmsException(
    GlobalErrorCode.SEX_MISMATCH
)
