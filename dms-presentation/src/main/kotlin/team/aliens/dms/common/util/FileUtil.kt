package team.aliens.dms.common.util

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

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
}
