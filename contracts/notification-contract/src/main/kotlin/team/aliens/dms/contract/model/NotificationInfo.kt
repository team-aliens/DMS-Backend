package team.aliens.dms.contract.model

import java.time.LocalDateTime
import java.util.UUID

data class NotificationInfo(

    val schoolId: UUID,

    val topic: Topic,

    val linkIdentifier: String?,

    val title: String,

    val content: String,

    val threadId: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val isSaveRequired: Boolean

)