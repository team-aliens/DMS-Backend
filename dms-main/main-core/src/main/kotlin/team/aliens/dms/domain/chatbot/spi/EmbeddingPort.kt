package team.aliens.dms.domain.chatbot.spi

interface EmbeddingPort {

    /**
     * 임베딩 모델 식별자. 차원까지 포함한다 (예: "gemini-embedding-001/768").
     * 문서에 저장해 두고, 값이 달라지면 그 문서의 청크를 전부 재임베딩한다.
     */
    fun embeddingModel(): String

    fun embedAll(texts: List<String>): List<FloatArray>
}
