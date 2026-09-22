package team.aliens.dms.domain.chatbot.model

// 청커가 문서를 헤딩 구조로 자른 한 조각
data class DocumentSection(
    val orderIndex: Int,
    val sectionPath: String,
    val content: String
) {
    // 임베딩에 넣는 텍스트. 경로가 앞에 붙어야 짧은 표 조각도 무슨 규정인지 알 수 있다.
    fun textForEmbedding(): String = "$sectionPath\n\n$content"
}
