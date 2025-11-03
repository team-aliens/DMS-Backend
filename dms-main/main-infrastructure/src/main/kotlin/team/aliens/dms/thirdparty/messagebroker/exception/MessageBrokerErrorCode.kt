package team.aliens.dms.thirdparty.messagebroker.exception

import team.aliens.dms.common.error.ErrorProperty

enum class MessageBrokerErrorCode (
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    UNKNOWN_MESSAGE_ERROR(500, "Unexpected message type in MessageBroker", 1);


    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "MESSAGE-BROKER-$status-$sequence"
}