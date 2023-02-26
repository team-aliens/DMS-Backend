package team.aliens.dms.domain.student

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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.dto.request.ResetStudentPasswordWebRequest
import team.aliens.dms.domain.student.dto.request.SignUpWebRequest
import team.aliens.dms.domain.student.dto.request.UpdateStudentProfileWebRequest
import team.aliens.dms.domain.student.dto.response.CheckStudentGcnResponse
import team.aliens.dms.domain.student.dto.response.FindStudentAccountIdResponse
import team.aliens.dms.domain.student.usecase.CheckDuplicatedAccountIdUseCase
import team.aliens.dms.domain.student.usecase.CheckDuplicatedEmailUseCase
import team.aliens.dms.domain.student.usecase.CheckStudentGcnUseCase
import team.aliens.dms.domain.student.usecase.FindStudentAccountIdUseCase
import team.aliens.dms.domain.student.usecase.ResetStudentPasswordUseCase
import team.aliens.dms.domain.student.usecase.SignUpUseCase
import team.aliens.dms.domain.student.usecase.StudentMyPageUseCase
import team.aliens.dms.domain.student.usecase.StudentWithdrawalUseCase
import team.aliens.dms.domain.student.usecase.UpdateStudentProfileUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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
    private val studentWithdrawalUseCase: StudentWithdrawalUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid request: SignUpWebRequest): SignUpResponse {
        val signUpRequest = SignUpRequest(
            schoolCode = request.schoolCode!!,
            schoolAnswer = request.schoolAnswer!!,
            email = request.email!!,
            authCode = request.authCode!!,
            grade = request.grade!!,
            classRoom = request.classRoom!!,
            number = request.number!!,
            accountId = request.accountId!!,
            password = request.password!!,
            profileImageUrl = request.profileImageUrl
        )

        return signUpUseCase.execute(signUpRequest)
    }

    @GetMapping("/email/duplication")
    fun checkDuplicatedEmail(@RequestParam @Email @NotBlank email: String?) {
        checkDuplicatedEmailUseCase.execute(email!!)
    }

    @GetMapping("/account-id/duplication")
    fun checkDuplicatedAccountId(@RequestParam("account_id") @NotBlank accountId: String?) {
        checkDuplicatedAccountIdUseCase.execute(accountId!!)
    }

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") @NotNull schoolId: UUID?,
        @RequestParam @NotBlank name: String?,
        @RequestParam @NotNull grade: Int?,
        @RequestParam("class_room") @NotNull classRoom: Int?,
        @RequestParam @NotNull number: Int?
    ): FindStudentAccountIdResponse {
        val request = FindStudentAccountIdRequest(
            name = name!!,
            grade = grade!!,
            classRoom = classRoom!!,
            number = number!!
        )

        val result = findStudentAccountIdUseCase.execute(schoolId!!, request)

        return FindStudentAccountIdResponse(result)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetStudentPasswordWebRequest) {
        val request = ResetStudentPasswordRequest(
            accountId = webRequest.accountId!!,
            name = webRequest.name!!,
            email = webRequest.email!!,
            authCode = webRequest.authCode!!,
            newPassword = webRequest.newPassword!!
        )

        resetStudentPasswordUseCase.execute(request)
    }

    @GetMapping("/name")
    fun checkGcn(
        @RequestParam("school_id") @NotNull schoolId: UUID?,
        @RequestParam @NotNull grade: Int?,
        @RequestParam("class_room") @NotNull classRoom: Int?,
        @RequestParam @NotNull number: Int?
    ): CheckStudentGcnResponse {
        val request = CheckStudentGcnRequest(
            schoolId = schoolId!!,
            grade = grade!!,
            classRoom = classRoom!!,
            number = number!!
        )

        val result = checkStudentGcnUseCase.execute(request)

        return CheckStudentGcnResponse(result)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/profile")
    fun updateProfile(@RequestBody @Valid webRequest: UpdateStudentProfileWebRequest) {
        updateStudentProfileUseCase.execute(webRequest.profileImageUrl!!)
    }

    @GetMapping("/profile")
    fun myPage(): StudentMyPageResponse {
        return studentMyPageUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun withdrawal() {
        studentWithdrawalUseCase.execute()
    }
}
   
