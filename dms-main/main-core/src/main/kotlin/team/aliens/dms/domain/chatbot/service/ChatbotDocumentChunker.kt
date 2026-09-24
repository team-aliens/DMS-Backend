package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.chatbot.model.DocumentSection

/**
 * 문서를 헤딩 구조로 자른다.
 *
 * - `##`/`###` 하나 = 조각 하나. 경로는 "대분류 > 소분류".
 * - MAX_CHUNK_CHARS를 넘는 조각만 볼드 소제목(**…**) → 문단 순으로 더 자른다.
 * - 고정 길이 슬라이딩·오버랩은 쓰지 않는다. 헤딩 경계가 의미 경계라 겹치면 오히려 흐려진다.
 * - "목차"처럼 제목만 나열된 섹션은 모든 질문에 걸리므로 뺀다.
 * - 헤딩이 하나도 없는 문서(txt 등)는 문서 제목을 경로로 쓰고 문단 단위로 자른다.
 */
@Service
class ChatbotDocumentChunker {

    fun chunk(title: String, markdown: String): List<DocumentSection> {
        val sections = splitByHeadings(markdown)
            .filter { it.body.isNotBlank() && it.lastTitle !in SKIPPED_TITLES }
            .flatMap { splitOversize(it.path, it.body) }
            .ifEmpty { splitWithoutHeadings(title, markdown) }

        return sections.mapIndexed { index, (path, body) ->
            DocumentSection(orderIndex = index, sectionPath = path, content = body)
        }
    }

    private data class RawSection(val path: String, val lastTitle: String, val body: String)

    private fun splitByHeadings(markdown: String): List<RawSection> {
        val result = mutableListOf<RawSection>()
        var h2: String? = null
        var h3: String? = null
        val buffer = StringBuilder()

        fun flush() {
            val path = listOfNotNull(h2, h3).joinToString(PATH_SEPARATOR)
            val last = h3 ?: h2
            if (path.isNotEmpty() && last != null) {
                result += RawSection(path, last, buffer.toString().trim())
            }
            buffer.clear()
        }

        markdown.lines().forEach { line ->
            when {
                line.startsWith("### ") -> {
                    flush()
                    h3 = line.removePrefix("### ").trim()
                }
                line.startsWith("## ") -> {
                    flush()
                    h2 = line.removePrefix("## ").trim()
                    h3 = null
                }
                line.startsWith("# ") || line.trim() == "---" -> Unit
                else -> buffer.appendLine(line)
            }
        }
        flush()
        return result
    }

    // 헤딩 기준으로 아무것도 안 나온 문서. H1·구분선만 걷어내고 본문 전체를 제목 아래 한 경로로 둔다
    private fun splitWithoutHeadings(title: String, markdown: String): List<Pair<String, String>> {
        val body = markdown.lines()
            .filterNot { it.startsWith("# ") || it.trim() == "---" }
            .joinToString("\n")
            .trim()

        if (body.isEmpty()) return emptyList()
        return splitOversize(title, body)
    }

    private fun splitOversize(path: String, body: String): List<Pair<String, String>> {
        if (body.length <= MAX_CHUNK_CHARS) return listOf(path to body)

        val byBold = splitByBoldTitles(path, body)
        return byBold.flatMap { (subPath, subBody) ->
            if (subBody.length <= MAX_CHUNK_CHARS) listOf(subPath to subBody)
            else splitByParagraphs(subBody).map { subPath to it }
        }
    }

    private fun splitByBoldTitles(path: String, body: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        var currentPath = path
        val buffer = StringBuilder()

        fun flush() {
            val text = buffer.toString().trim()
            if (text.isNotEmpty()) result += currentPath to text
            buffer.clear()
        }

        body.lines().forEach { line ->
            val bold = BOLD_TITLE.matchEntire(line.trim())
            if (bold != null) {
                flush()
                currentPath = path + PATH_SEPARATOR + bold.groupValues[1].trim()
            } else {
                buffer.appendLine(line)
            }
        }
        flush()
        return result
    }

    private fun splitByParagraphs(body: String): List<String> {
        val result = mutableListOf<String>()
        val buffer = StringBuilder()
        body.split(PARAGRAPH_SEPARATOR).map { it.trim() }.filter { it.isNotEmpty() }.forEach { paragraph ->
            if (buffer.isNotEmpty() && buffer.length + paragraph.length > MAX_CHUNK_CHARS) {
                result += buffer.toString().trim()
                buffer.clear()
            }
            if (buffer.isNotEmpty()) buffer.append("\n\n")
            buffer.append(paragraph)
        }
        if (buffer.isNotEmpty()) result += buffer.toString().trim()
        return result
    }

    companion object {
        const val MAX_CHUNK_CHARS = 1200
        private const val PATH_SEPARATOR = " > "
        private val SKIPPED_TITLES = setOf("목차")
        private val BOLD_TITLE = Regex("""\*\*(.+?)\*\*""")
        private val PARAGRAPH_SEPARATOR = Regex("""\n\s*\n""")
    }
}
