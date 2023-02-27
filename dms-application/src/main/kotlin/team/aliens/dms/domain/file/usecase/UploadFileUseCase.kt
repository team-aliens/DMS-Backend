package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.spi.UploadFilePort
import java.io.File

@ReadOnlyUseCase
class UploadFileUseCase(
    private val uploadFilePort: UploadFilePort
) {

    fun execute(file: File): String {


        return uploadFilePort.upload(file)
    }
/*
    internal fun File.isCorrectExtension(file: File) = when (file.extension.lowercase()) {
        "jpg", "jpeg", "png", "heic" -> true
        else -> false
    }*/
}
