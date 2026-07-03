package team.aliens.dms.thirdparty.ai

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.exception.RegulationDocumentNotFoundException
import team.aliens.dms.domain.chatbot.spi.RegulationDocumentPort

@Component
class RegulationDocumentAdapter : RegulationDocumentPort {

    /**
     * 규정 문서는 자주 바뀌지 않으므로 최초 1회 읽어 메모리에 캐싱한다.
     * 1차: 클래스패스 리소스(regulations/dormitory-rules.md) 전문을 그대로 반환(stuffing).
     * 2차: 이 어댑터만 검색 기반(RAG) 구현으로 교체하면 코어/유스케이스는 변경되지 않는다.
     */
    private val regulation: String by lazy { loadFromClasspath() }

    override fun loadRegulation(): String = regulation

    private fun loadFromClasspath(): String {
        val resource = ClassPathResource(REGULATION_PATH)
        if (!resource.exists()) {
            throw RegulationDocumentNotFoundException
        }
        return resource.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
    }

    companion object {
        private const val REGULATION_PATH = "regulations/dormitory-rules.md"
    }
}
