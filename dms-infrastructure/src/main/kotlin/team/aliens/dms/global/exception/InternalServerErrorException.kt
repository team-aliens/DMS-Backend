package team.aliens.dms.global.exception

import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object InternalServerErrorException : DmsException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
) {

    @JvmField
    val EXCEPTION = InternalServerErrorException
}