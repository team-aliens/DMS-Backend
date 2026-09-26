package team.aliens.dms.domain.chatbot.spi.vo

import java.util.UUID

// 유사도 검색으로 뽑힌 청크. 프롬프트 조립에 필요한 값만 담는다(임베딩 벡터는 읽지 않는다)
data class RetrievedChunkVO(
    val chunkId: UUID,
    val documentTitle: String,
    val sectionPath: String,
    val content: String,
    // 질문 임베딩과의 코사인 거리(<=>). 작을수록 가깝다
    val distance: Double
)
