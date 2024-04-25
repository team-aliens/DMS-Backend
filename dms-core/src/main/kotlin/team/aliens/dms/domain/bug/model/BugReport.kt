package team.aliens.dms.domain.bug.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.*

@Aggregate
data class BugReport(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val content: String,

    val developmentArea: DevelopmentArea,

    val createdAt: LocalDateTime?,

    val attachmentUrls: BugAttachment?
)
