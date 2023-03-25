package team.aliens.dms.common.util

import org.springframework.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID
import javax.servlet.http.HttpServletResponse

object FileUtil {
    val transferFile = { multipartFile: MultipartFile ->
        File("${UUID.randomUUID()}||${multipartFile.originalFilename}").let {
            FileOutputStream(it).run {
                this.write(multipartFile.bytes)
                this.close()
            }
            it
        }
    }

    fun HttpServletResponse.setExcelContentDisposition(fileName: String) {
        setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=${URLEncoder.encode(fileName, "UTF-8")}.xlsx"
        )
    }
}
