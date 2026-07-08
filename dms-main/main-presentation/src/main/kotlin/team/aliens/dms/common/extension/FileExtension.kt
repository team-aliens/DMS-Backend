package team.aliens.dms.common.extension

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID

fun MultipartFile.toFile(): File {
    val tempDir = System.getProperty("java.io.tmpdir")
    val file = File(tempDir, "${UUID.randomUUID()}_$originalFilename")
    FileOutputStream(file).use { it.write(bytes) }
    return file
}

fun HttpServletResponse.setExcelContentDisposition(fileName: String) {
    setHeader(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=${URLEncoder.encode(fileName, "UTF-8")}.xlsx"
    )
}
