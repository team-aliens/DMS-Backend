package team.aliens.dms.domain.file

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.domain.file.usecase.UploadFileUseCase
import team.aliens.dms.domain.file.dto.response.UploadFileResponse
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

@Validated
@RequestMapping("/files")
@RestController
class FileWebAdapter(
    private val uploadFileUseCase: UploadFileUseCase
) {

    @PostMapping
    fun uploadFile(@RequestPart @NotNull file: MultipartFile?): UploadFileResponse {
        val result = uploadFileUseCase.execute(
            file!!.let(transferFile)
        )

        return UploadFileResponse(result)
    }

    private val transferFile = { multipartFile: MultipartFile ->
        File("${UUID.randomUUID()}||${multipartFile.originalFilename}").let {
            FileOutputStream(it).run {
                this.write(multipartFile.bytes)
                this.close()
            }
            it
        }
    }
}