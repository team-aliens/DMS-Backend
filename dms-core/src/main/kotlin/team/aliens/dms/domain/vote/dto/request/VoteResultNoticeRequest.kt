package team.aliens.dms.domain.vote.dto.request

import java.time.LocalDateTime
import java.util.*

data class VoteResultNoticeRequest(
    val managerId: UUID,
    val title:String,
    val content:String,

)
