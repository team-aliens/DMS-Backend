package team.aliens.dms.persistence.chatbot.repository.vo

import java.util.UUID

// 유사도 검색 네이티브 쿼리의 인터페이스 프로젝션. 게터 이름이 SELECT 의 별칭과 같아야 한다
interface RetrievedChunkVO {
    val chunkId: UUID
    val documentTitle: String
    val sectionPath: String
    val content: String
    val distance: Double
}
