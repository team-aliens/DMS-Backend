package team.aliens.dms.domain.chatbot.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class ChatbotDocument(

    val id: UUID = UUID(0, 0),

    val title: String,

    // 올린 파일 이름. (schoolId, sourceUri) 가 문서의 키라서 같은 이름으로 다시 올리면 교체된다
    val sourceUri: String,

    val contentHash: String,

    val embeddingModel: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime,

    // 원문의 개정일. 올리는 사람이 적고, 안 적으면 올린 날
    val publishedAt: LocalDateTime,

    override val schoolId: UUID

) : SchoolIdDomain {

    /** 같은 원문을 같은 임베딩 모델로 이미 인제스천한 상태인지. true면 청킹·임베딩을 다시 할 필요가 없다 */
    fun isUpToDate(contentHash: String, embeddingModel: String): Boolean =
        this.contentHash == contentHash && this.embeddingModel == embeddingModel

    companion object {
        val ALLOWED_EXTENSIONS = setOf("md", "txt")

        private const val H1_PREFIX = "# "
        // tbl_chatbot_document.title VARCHAR(100)
        private const val TITLE_MAX_LENGTH = 100

        fun isAllowedExtension(fileName: String): Boolean =
            fileName.substringAfterLast('.', "").lowercase() in ALLOWED_EXTENSIONS

        // 마크다운 첫 H1 을 제목으로, 없으면 확장자를 뗀 파일 이름
        fun titleOf(fileName: String, content: String): String =
            content.lineSequence()
                .firstOrNull { it.startsWith(H1_PREFIX) }
                ?.removePrefix(H1_PREFIX)?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?.take(TITLE_MAX_LENGTH)
                ?: fileName.substringBeforeLast('.').take(TITLE_MAX_LENGTH)

        // 원문 SHA-256 hex. 같은 값이면 재인제스천을 건너뛴다
        fun contentHashOf(content: String): String =
            MessageDigest.getInstance("SHA-256")
                .digest(content.toByteArray(Charsets.UTF_8))
                .joinToString("") { "%02x".format(it) }
    }
}
