package team.aliens.dms.domain.chatbot.spi

interface EmbeddingPort {

    /**
     * 임베딩 모델 식별자. 차원까지 포함한다 (예: "gemini-embedding-001/768").
     * 문서에 저장해 두고, 값이 달라지면 그 문서의 청크를 전부 재임베딩한다.
     */
    fun embeddingModel(): String

    // 색인용 문서 임베딩(taskType = RETRIEVAL_DOCUMENT)
    fun embedAll(texts: List<String>): List<FloatArray>

    // 검색용 질문 임베딩(taskType = RETRIEVAL_QUERY). 문서 벡터와 같은 방식으로 L2 정규화한다
    fun embedQuery(text: String): FloatArray
}
