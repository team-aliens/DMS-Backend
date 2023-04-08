package team.aliens.dms.common.extension

import org.springframework.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID
import javax.servlet.http.HttpServletResponse

fun MultipartFile.toFile() =
    File("${UUID.randomUUID()}||$originalFilename").let {
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
