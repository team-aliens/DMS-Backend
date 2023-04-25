package team.aliens.dms.thirdparty.parser.exception

import team.aliens.dms.common.error.DynamicDmsException
import team.aliens.dms.common.util.StringUtil.toStringWithoutBracket
import team.aliens.dms.global.error.GlobalErrorCode

class BadExcelFormatException(
    invalidRowIdxes: List<Int>
) : DynamicDmsException(
    status = GlobalErrorCode.BAD_EXCEL_FORMAT.status(),
    message = GlobalErrorCode.BAD_EXCEL_FORMAT.message().format(
        invalidRowIdxes.take(3).toStringWithoutBracket(),
        invalidRowIdxes.size
    ),
    code = GlobalErrorCode.BAD_EXCEL_FORMAT.code()
)
