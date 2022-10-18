package team.aliens.dms.global.exception

import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

/**
 *
 * Internal Server Error 를 발생시키는 InternalServerErrorException
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
object InternalServerErrorException : DmsException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)