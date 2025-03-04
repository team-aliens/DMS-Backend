package team.aliens.dms.domain.student

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import team.aliens.dms.common.extension.setExcelContentDisposition
import team.aliens.dms.common.extension.toFile
import team.aliens.dms.domain.auth.dto.TokenFeatureResponse
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.StudentDetailsResponse
import team.aliens.dms.domain.student.dto.StudentEmailResponse
import team.aliens.dms.domain.student.dto.StudentNameResponse
import team.aliens.dms.domain.student.dto.StudentResponse
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.dto.request.ResetStudentPasswordWebRequest
import team.aliens.dms.domain.student.dto.request.SignUpWebRequest
import team.aliens.dms.domain.student.dto.request.UpdateStudentProfileWebRequest
import team.aliens.dms.domain.student.usecase.CheckDuplicatedAccountIdUseCase
import team.aliens.dms.domain.student.usecase.CheckDuplicatedEmailUseCase
import team.aliens.dms.domain.student.usecase.CheckStudentGcnUseCase
import team.aliens.dms.domain.student.usecase.ExportStudentUseCase
import team.aliens.dms.domain.student.usecase.FindStudentAccountIdUseCase
import team.aliens.dms.domain.student.usecase.GetModelStudentsUseCase
import team.aliens.dms.domain.student.usecase.ImportStudentUseCase
import team.aliens.dms.domain.student.usecase.ManagerGetAllStudentsUseCase
import team.aliens.dms.domain.student.usecase.QueryStudentDetailsUseCase
import team.aliens.dms.domain.student.usecase.RemoveStudentUseCase
import team.aliens.dms.domain.student.usecase.ResetStudentPasswordUseCase
import team.aliens.dms.domain.student.usecase.SignUpUseCase
import team.aliens.dms.domain.student.usecase.StudentGetAllStudentsUseCase
import team.aliens.dms.domain.student.usecase.StudentMyPageUseCase
import team.aliens.dms.domain.student.usecase.StudentWithdrawalUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentGcnByFileUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentProfileUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentRoomByFileUseCase
import team.aliens.dms.domain.vote.dto.response.ModelStudentsResponse
import java.time.LocalDate
import java.util.UUID

@Validated
@RequestMapping("/students")
@RestController
class StudentWebAdapter(
    private val signUpUseCase: SignUpUseCase,
    private val checkDuplicatedEmailUseCase: CheckDuplicatedEmailUseCase,
    private val checkDuplicatedAccountIdUseCase: CheckDuplicatedAccountIdUseCase,
    private val findStudentAccountIdUseCase: FindStudentAccountIdUseCase,
    private val resetStudentPasswordUseCase: ResetStudentPasswordUseCase,
    private val checkStudentGcnUseCase: CheckStudentGcnUseCase,
    private val updateStudentProfileUseCase: UpdateStudentProfileUseCase,
    private val studentMyPageUseCase: StudentMyPageUseCase,
    private val studentWithdrawalUseCase: StudentWithdrawalUseCase,
    private val exportStudentUseCase: ExportStudentUseCase,
    private val managerGetAllStudentsUseCase: ManagerGetAllStudentsUseCase,
    private val queryStudentDetailsUseCase: QueryStudentDetailsUseCase,
    private val removeStudentUseCase: RemoveStudentUseCase,
    private val updateStudentGcnByFileUseCase: UpdateStudentGcnByFileUseCase,
    private val updateStudentRoomByFileUseCase: UpdateStudentRoomByFileUseCase,
    private val importStudentUseCase: ImportStudentUseCase,
    private val studentGetAllStudentsUseCase: StudentGetAllStudentsUseCase,
    private val getModelStudentsUseCase: GetModelStudentsUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid request: SignUpWebRequest): TokenFeatureResponse {
        val signUpRequest = SignUpRequest(
            schoolCode = request.schoolCode,
            schoolAnswer = request.schoolAnswer,
            email = request.email,
            authCode = request.authCode,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number,
            accountId = request.accountId,
            password = request.password,
            profileImageUrl = request.profileImageUrl
        )

        return signUpUseCase.execute(signUpRequest)
    }

    @GetMapping("/email/duplication")
    fun checkDuplicatedEmail(@RequestParam @Email @NotBlank email: String) {
        checkDuplicatedEmailUseCase.execute(email)
    }

    @GetMapping("/account-id/duplication")
    fun checkDuplicatedAccountId(@RequestParam("account_id") @NotBlank accountId: String) {
        checkDuplicatedAccountIdUseCase.execute(accountId)
    }

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") @NotNull schoolId: UUID,
        @RequestParam @NotBlank name: String,
        @RequestParam @NotNull grade: Int,
        @RequestParam("class_room") @NotNull classRoom: Int,
        @RequestParam @NotNull number: Int
    ): StudentEmailResponse {
        val request = FindStudentAccountIdRequest(
            name = name,
            grade = grade,
            classRoom = classRoom,
            number = number
        )

        return findStudentAccountIdUseCase.execute(schoolId, request)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetStudentPasswordWebRequest) {
        val request = ResetStudentPasswordRequest(
            accountId = webRequest.accountId,
            name = webRequest.name,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword
        )

        resetStudentPasswordUseCase.execute(request)
    }

    @GetMapping("/name")
    fun checkGcn(
        @RequestParam("school_id") @NotNull schoolId: UUID,
        @RequestParam @NotNull grade: Int,
        @RequestParam("class_room") @NotNull classRoom: Int,
        @RequestParam @NotNull number: Int
    ): StudentNameResponse {
        val request = CheckStudentGcnRequest(
            schoolId = schoolId,
            grade = grade,
            classRoom = classRoom,
            number = number
        )

        return checkStudentGcnUseCase.execute(request)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/profile")
    fun updateProfile(@RequestBody @Valid webRequest: UpdateStudentProfileWebRequest) {
        updateStudentProfileUseCase.execute(webRequest.profileImageUrl)
    }

    @GetMapping("/profile")
    fun myPage(): StudentResponse {
        return studentMyPageUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun withdrawal() {
        studentWithdrawalUseCase.execute()
    }

    @GetMapping("/file")
    fun exportStudentInfo(httpResponse: HttpServletResponse): ByteArray {
        val response = exportStudentUseCase.execute()
        httpResponse.setExcelContentDisposition(response.fileName)
        return response.file
    }

    @GetMapping("/manager")
    fun managerGetAllStudents(
        @RequestParam(required = false) name: String?,
        @RequestParam @NotNull sort: Sort,
        @RequestParam(name = "filter_type", required = false) filterType: PointFilterType?,
        @RequestParam(name = "min_point", required = false) minPoint: Int?,
        @RequestParam(name = "max_point", required = false) maxPoint: Int?,
        @RequestParam(name = "tag_id", required = false) tagIds: List<UUID>?
    ): StudentsResponse {
        return managerGetAllStudentsUseCase.execute(
            name = name,
            sort = sort,
            pointFilter = PointFilter(
                filterType = filterType,
                minPoint = minPoint,
                maxPoint = maxPoint
            ),
            tagIds = tagIds
        )
    }

    @GetMapping("/{student-id}")
    fun getStudentDetails(@PathVariable("student-id") @NotNull studentId: UUID): StudentDetailsResponse {
        return queryStudentDetailsUseCase.execute(studentId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{student-id}")
    fun deleteStudent(@PathVariable("student-id") @NotNull studentId: UUID) {
        removeStudentUseCase.execute(studentId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/file/room")
    fun updateStudentRoomByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentRoomByFileUseCase.execute(file!!.toFile())
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/file/gcn")
    fun updateStudentGcnByFile(@RequestPart @NotNull file: MultipartFile?) {
        updateStudentGcnByFileUseCase.execute(file!!.toFile())
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/verified-student")
    fun importVerifiedStudentFromExcel(@RequestPart @NotNull file: MultipartFile?) {
        importStudentUseCase.execute(
            file!!.toFile()
        )
    }

    @GetMapping
    fun studentGetAllStudents(@RequestParam(required = false) name: String?): StudentsResponse {
        return studentGetAllStudentsUseCase.execute(name)
    }

    @GetMapping("/candidate-list")
    fun getModelStudents(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): ModelStudentsResponse {
        val students = getModelStudentsUseCase.execute(date)
        return ModelStudentsResponse(students = students)
    }
}
