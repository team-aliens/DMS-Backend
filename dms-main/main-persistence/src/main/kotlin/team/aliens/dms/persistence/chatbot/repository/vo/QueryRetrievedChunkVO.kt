package team.aliens.dms.persistence.chatbot.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.chatbot.spi.vo.RetrievedChunkVO
import java.util.UUID

class QueryRetrievedChunkVO @QueryProjection constructor(
    chunkId: UUID,
    documentTitle: String,
    sectionPath: String,
    content: String,
    distance: Double
) : RetrievedChunkVO(
    chunkId = chunkId,
    documentTitle = documentTitle,
    sectionPath = sectionPath,
    content = content,
    distance = distance
)
