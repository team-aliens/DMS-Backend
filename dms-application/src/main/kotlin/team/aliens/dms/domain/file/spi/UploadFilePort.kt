package team.aliens.dms.domain.file.spi

import java.io.File

interface UploadFilePort {

    fun upload(file: File): String
}
