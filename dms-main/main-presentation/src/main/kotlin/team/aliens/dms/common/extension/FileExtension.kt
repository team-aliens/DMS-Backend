package team.aliens.dms.common.extension

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID

fun MultipartFile.toFile() =
    File("${UUID.randomUUID()}_$originalFilename").let {
        FileOutputStream(it).run {
            this.write(bytes)
            this.close()
        }
        it
    }

fun HttpServletResponse.setExcelContentDisposition(fileName: String) {
    setHeader(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=${URLEncoder.encode(fileName, "UTF-8")}.xlsx"
    )
}
