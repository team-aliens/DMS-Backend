package team.aliens.dms.domain.file.spi

import java.io.File

interface UploadFilePort {

    fun upload(file: File): String
    fun getResourceUrl(fileName: String): String
    fun getUploadUrl(fileName: String): String
}
