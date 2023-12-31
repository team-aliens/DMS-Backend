package team.aliens.dms.domain.manager

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.manager.dto.ManagerDetailsResponse
import team.aliens.dms.domain.manager.dto.ManagerEmailResponse
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.dto.request.ResetPasswordManagerWebRequest
import team.aliens.dms.domain.manager.usecase.FindManagerAccountIdUseCase
import team.aliens.dms.domain.manager.usecase.ManagerMyPageUseCase
import team.aliens.dms.domain.manager.usecase.ResetManagerPasswordUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.common.extension.toFile
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.dto.StudentDetailsResponse
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.usecase.QueryStudentDetailsUseCase
import team.aliens.dms.domain.student.usecase.QueryStudentsUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentGcnByFileUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentRoomByFileUseCase

@Validated
@RequestMapping("/managers")
@RestController
class ManagerWebAdapter(
    private val findManagerAccountIdUseCase: FindManagerAccountIdUseCase,
    private val resetManagerPasswordUseCase: ResetManagerPasswordUseCase,
    private val managerMyPageUseCase: ManagerMyPageUseCase,
    private val queryStudentDetailsUseCase: QueryStudentDetailsUseCase,
    private val updateStudentGcnByFileUseCase: UpdateStudentGcnByFileUseCase,
    private val updateStudentRoomByFileUseCase: UpdateStudentRoomByFileUseCase,
    private val queryStudentsUseCase: QueryStudentsUseCase,
) {

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") @NotNull schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ): ManagerEmailResponse {
        return findManagerAccountIdUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetPasswordManagerWebRequest) {
        val request = ResetManagerPasswordRequest(
            accountId = webRequest.accountId,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword
        )

        resetManagerPasswordUseCase.execute(request)
    }

    @GetMapping("/profile")
    fun myPage(): ManagerDetailsResponse {
        return managerMyPageUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/students/file/room")
    fun updateStudentRoomByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentRoomByFileUseCase.execute(file!!.toFile())
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/students/file/gcn")
    fun updateStudentGcnByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentGcnByFileUseCase.execute(file!!.toFile())
    }

    @GetMapping("/")
    fun getStudentstest(
        @RequestParam(required = false) name: String?,
        @RequestParam @NotNull sort: Sort,
        @RequestParam(name = "filter_type", required = false) filterType: PointFilterType?,
        @RequestParam(name = "min_point", required = false) minPoint: Int?,
        @RequestParam(name = "max_point", required = false) maxPoint: Int?,
        @RequestParam(name = "tag_id", required = false) tagIds: List<UUID>?
    ): StudentsResponse {
        return queryStudentsUseCase.execute(
            name = name,
            sort = sort,
            filterType = filterType,
            minPoint = minPoint,
            maxPoint = maxPoint,
            tagIds = tagIds
        )
    }

    @GetMapping("/students")
    fun getStudents(
        @RequestParam(required = false) name: String?,
        @RequestParam @NotNull sort: Sort,
        @RequestParam(name = "filter_type", required = false) filterType: PointFilterType?,
        @RequestParam(name = "min_point", required = false) minPoint: Int?,
        @RequestParam(name = "max_point", required = false) maxPoint: Int?,
        @RequestParam(name = "tag_id", required = false) tagIds: List<UUID>?
    ): StudentsResponse {
        return queryStudentsUseCase.execute(
            name = name,
            sort = sort,
            filterType = filterType,
            minPoint = minPoint,
            maxPoint = maxPoint,
            tagIds = tagIds
        )
    }

    @GetMapping("/{student-id}")
    fun getStudentDetails(@PathVariable("student-id") @NotNull studentId: UUID): StudentDetailsResponse {
        return queryStudentDetailsUseCase.execute(studentId)
    }

    @GetMapping("/students/{student-id}")
    fun getStudentDetails2(@PathVariable("student-id") @NotNull studentId: UUID): StudentDetailsResponse {
        return queryStudentDetailsUseCase.execute(studentId)
    }
}
