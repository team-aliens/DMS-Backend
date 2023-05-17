package team.aliens.dms.thirdparty.parser.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.common.util.StringUtil.toStringWithoutBracket

object ExcelInvalidFileException : DmsException(
    ParserErrorCode.INVALID_FILE
)

object ExcelExtensionMismatchException : DmsException(
    ParserErrorCode.EXTENSION_MISMATCH
)

class BadExcelFormatException(
    invalidRowIdxes: List<Int>
) : DmsException(
    ParserErrorCode.BAD_EXCEL_FORMAT
        .formatMessage(
            invalidRowIdxes.take(3).toStringWithoutBracket(),
            invalidRowIdxes.size.toString()
        )
)
