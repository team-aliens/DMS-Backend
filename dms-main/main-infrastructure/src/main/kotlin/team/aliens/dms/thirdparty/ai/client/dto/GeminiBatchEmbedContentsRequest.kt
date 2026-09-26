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
        // 색인용 문서 임베딩
        private const val TASK_TYPE_DOCUMENT = "RETRIEVAL_DOCUMENT"

        // 검색용 질문 임베딩. 문서 쪽(RETRIEVAL_DOCUMENT)과 짝을 이뤄야 검색 품질이 나온다
        private const val TASK_TYPE_QUERY = "RETRIEVAL_QUERY"

        fun ofDocuments(model: String, dimension: Int, texts: List<String>) = GeminiBatchEmbedContentsRequest(
            requests = texts.map { embedRequest(model, dimension, it, TASK_TYPE_DOCUMENT) }
        )

        fun ofQuery(model: String, dimension: Int, text: String) = GeminiBatchEmbedContentsRequest(
            requests = listOf(embedRequest(model, dimension, text, TASK_TYPE_QUERY))
        )

        private fun embedRequest(model: String, dimension: Int, text: String, taskType: String) = EmbedRequest(
            model = "models/$model",
            content = Content(parts = listOf(Part(text))),
            taskType = taskType,
            outputDimensionality = dimension
        )
    }
}
