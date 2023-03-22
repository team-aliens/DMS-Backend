package team.aliens.dms.domain.file.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.FileUtil
import team.aliens.dms.domain.file.dto.GetFileUploadUrlResponse
import team.aliens.dms.domain.file.exception.FileInvalidExtensionException
import team.aliens.dms.domain.file.spi.UploadFilePort

@ReadOnlyUseCase
class GetFileUploadUrlUseCase(
    val filePort: UploadFilePort
) {
    fun execute(fileName: String): GetFileUploadUrlResponse {

        val extension = fileName.substring(fileName.lastIndexOf(".").inc())
        if (!FileUtil.isCorrectExtension(extension)) {
            throw FileInvalidExtensionException
        }

        val fileNameToSave = "${UUID.randomUUID()}||${fileName}"

        return GetFileUploadUrlResponse(
            fileUploadUrl = filePort.getUploadUrl(fileNameToSave),
            fileUrl = filePort.getResourceUrl(fileNameToSave)
        )
    }
}