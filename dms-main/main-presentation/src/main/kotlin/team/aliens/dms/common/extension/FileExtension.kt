package team.aliens.dms.common.extension

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction
import java.util.UUID

fun MultipartFile.toFile(): File {
    val tempDir = System.getProperty("java.io.tmpdir")
    val file = File(tempDir, "${UUID.randomUUID()}_$originalFilename")
    FileOutputStream(file).use { it.write(bytes) }
    return file
}

private val MS949: Charset = Charset.forName("MS949")
private const val BOM = '\uFEFF'

/**
 * 텍스트 파일(md/txt)을 문자열로 읽는다.
 * UTF-8 로 먼저 시도하고, 깨지면 CP949 로 읽는다 — 구형 윈도우 메모장이 저장한 txt 대비. BOM 은 뗀다
 */
fun MultipartFile.readText(): String {
    val text = runCatching {
        Charsets.UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
            .decode(ByteBuffer.wrap(bytes))
            .toString()
    }.getOrElse { e ->
        if (e is CharacterCodingException) String(bytes, MS949) else throw e
    }
    return text.removePrefix(BOM.toString())
}

fun HttpServletResponse.setExcelContentDisposition(fileName: String) {
    setHeader(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=${URLEncoder.encode(fileName, "UTF-8")}.xlsx"
    )
}
