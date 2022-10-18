package team.aliens.dms.file

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.domain.file.usecase.UploadImageUseCase
import team.aliens.dms.file.dto.response.UploadImageResponse
import java.io.File
import java.io.FileOutputStream
import java.util.*

@RequestMapping("/files")
@RestController
class FileWebAdapter(
    private val uploadImageUseCase: UploadImageUseCase
) {

    @PostMapping
    fun uploadFile(@RequestPart file: MultipartFile): UploadImageResponse {
        val result = uploadImageUseCase.execute(file.let(transferFile))

        return UploadImageResponse(result)
    }

    private val transferFile = { multipartFile: MultipartFile ->
        File("${UUID.randomUUID()}@${multipartFile.originalFilename}").let {
            FileOutputStream(it).run {
                this.write(multipartFile.bytes)
                this.close()
            }
            it
        }
    }

}