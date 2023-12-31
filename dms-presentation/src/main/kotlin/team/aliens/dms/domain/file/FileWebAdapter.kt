package team.aliens.dms.domain.file

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.common.extension.toFile
import team.aliens.dms.domain.file.dto.GetFileUploadUrlResponse
import team.aliens.dms.domain.file.dto.response.UploadFileResponse
import team.aliens.dms.domain.file.usecase.GetFileUploadUrlUseCase
import team.aliens.dms.domain.file.usecase.UploadFileUseCase
import javax.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import team.aliens.dms.domain.student.usecase.ImportStudentUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentGcnByFileUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentRoomByFileUseCase

@Validated
@RequestMapping("/files")
@RestController
class FileWebAdapter(
    private val uploadFileUseCase: UploadFileUseCase,
    private val getFileUploadUrlUseCase: GetFileUploadUrlUseCase,
    private val importStudentUseCase: ImportStudentUseCase,
    private val updateStudentRoomByFileUseCase: UpdateStudentRoomByFileUseCase,
    private val updateStudentGcnByFileUseCase: UpdateStudentGcnByFileUseCase
) {

    @PostMapping
    fun uploadFile(@RequestPart @NotNull file: MultipartFile?): UploadFileResponse {
        val result = uploadFileUseCase.execute(
            file!!.toFile()
        )
        return UploadFileResponse(result)
    }

    @GetMapping("/url")
    fun getFileUploadUrl(
        @RequestParam("file_name") @NotNull fileName: String?
    ): GetFileUploadUrlResponse {
        return getFileUploadUrlUseCase.execute(fileName!!)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/room")
    fun updateStudentRoomByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentRoomByFileUseCase.execute(file!!.toFile())
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/gcn")
    fun updateStudentGcnByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentGcnByFileUseCase.execute(file!!.toFile())
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/file")
    fun importVerifiedStudentFromExcel2(@RequestPart @NotNull file: MultipartFile?) {
        importStudentUseCase.execute(
            file!!.toFile()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/verified-student")
    fun importVerifiedStudentFromExcel(@RequestPart @NotNull file: MultipartFile?) {
        importStudentUseCase.execute(
            file!!.toFile()
        )
    }
}
