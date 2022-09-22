package team.aliens.dms.global.error

import org.springframework.http.HttpStatus

/**
 *
 * 전역으로 사용되는 ErrorCode 를 모아둔 GlobalErrorCode
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
enum class GlobalErrorCode(
    private val status: HttpStatus,
    private val message: String
) : ErrorProperty {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    override fun status(): Int = status.value()
    override fun message(): String = message
}