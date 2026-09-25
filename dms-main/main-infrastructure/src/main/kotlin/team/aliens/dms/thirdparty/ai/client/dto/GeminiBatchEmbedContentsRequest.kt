package team.aliens.dms.thirdparty.ai.client.dto

/**
 * Gemini batchEmbedContents 요청 바디. 요청마다 model 을 다시 적어야 한다(API 규격).
 * 필드명은 전역 ObjectMapper 가 snake_case 로 내보내지만 Gemini 는 camelCase/snake_case 둘 다 받는다.
 */
data class GeminiBatchEmbedContentsRequest(
    val requests: List<EmbedRequest>
) {
    data class EmbedRequest(
        val model: String,
        val content: Content,
        val taskType: String,
        val outputDimensionality: Int
    )

    data class Content(
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )

    companion object {
        // 색인용 문서 임베딩. 질의 쪽은 RETRIEVAL_QUERY 로 따로 요청한다
        private const val TASK_TYPE_DOCUMENT = "RETRIEVAL_DOCUMENT"

        fun ofDocuments(model: String, dimension: Int, texts: List<String>) = GeminiBatchEmbedContentsRequest(
            requests = texts.map {
                EmbedRequest(
                    model = "models/$model",
                    content = Content(parts = listOf(Part(it))),
                    taskType = TASK_TYPE_DOCUMENT,
                    outputDimensionality = dimension
                )
            }
        )
    }
}
