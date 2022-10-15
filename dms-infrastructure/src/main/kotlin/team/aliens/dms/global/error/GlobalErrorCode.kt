package team.aliens.dms.global.error

/**
 *
 * 전역으로 사용되는 ErrorCode 를 모아둔 GlobalErrorCode
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    BAD_REQUEST(404, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    override fun status(): Int = status
    override fun message(): String = message
}