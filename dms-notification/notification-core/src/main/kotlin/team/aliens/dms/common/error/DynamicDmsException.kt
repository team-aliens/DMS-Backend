
package team.aliens.dms.common.error

abstract class DynamicDmsException(
    status: Int,
    message: String,
    code: String
) : DmsException(object : ErrorProperty {
    override fun status() = status
    override fun message() = message
    override fun code() = code
})
