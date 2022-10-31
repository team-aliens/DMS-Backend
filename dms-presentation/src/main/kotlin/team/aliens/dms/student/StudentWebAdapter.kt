package team.aliens.dms.student

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.student.dto.*
import team.aliens.dms.domain.student.usecase.*
import team.aliens.dms.student.dto.request.CheckStudentGcnWebRequest
import team.aliens.dms.student.dto.request.FindStudentAccountIdWebRequest
import team.aliens.dms.student.dto.request.ResetStudentPasswordWebRequest
import team.aliens.dms.student.dto.request.SignUpWebRequest
import team.aliens.dms.student.dto.response.CheckStudentGcnResponse
import team.aliens.dms.student.dto.response.FindStudentAccountIdResponse
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/students")
@RestController
class StudentWebAdapter(
    private val signUpUseCase: SignUpUseCase,
    private val checkDuplicatedEmailUseCase: CheckDuplicatedEmailUseCase,
    private val checkDuplicatedAccountIdUseCase: CheckDuplicatedAccountIdUseCase,
    private val findStudentAccountIdUseCase: FindStudentAccountIdUseCase,
    private val resetStudentPasswordUseCase: ResetStudentPasswordUseCase,
    private val checkStudentGcnUseCase: CheckStudentGcnUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid request: SignUpWebRequest): SignUpResponse {
        val signUpRequest = SignUpRequest(
            schoolCode = request.schoolCode,
            schoolAnswer = request.schoolAnswer,
            email = request.email,
            authCode = request.authCode,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number,
            accountId = request.accountId,
            password = request.password.value,
            profileImageUrl = request.profileImageUrl
        )

        return signUpUseCase.execute(signUpRequest)
    }

    @GetMapping("/email/duplication")
    fun checkDuplicatedEmail(@RequestParam @NotBlank email: String) {
        checkDuplicatedEmailUseCase.execute(email)
    }

    @GetMapping("/account-id/duplication")
    fun checkDuplicatedAccountId(@RequestParam @NotBlank accountId: String) {
        checkDuplicatedAccountIdUseCase.execute(accountId)
    }

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable(name = "school-id") schoolId: UUID,
        @ModelAttribute webRequest: FindStudentAccountIdWebRequest
    ): FindStudentAccountIdResponse {
        val request = FindStudentAccountIdRequest(
            name = webRequest.name,
            grade = webRequest.grade,
            classRoom = webRequest.classRoom,
            number = webRequest.number
        )

        val result = findStudentAccountIdUseCase.execute(schoolId, request)

        return FindStudentAccountIdResponse(result)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetStudentPasswordWebRequest) {
        val request = ResetStudentPasswordRequest(
            accountId = webRequest.accountId,
            name = webRequest.name,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword.value
        )

        resetStudentPasswordUseCase.execute(request)
    }

    @GetMapping("/name")
    fun checkGcn(@ModelAttribute @Valid webRequest: CheckStudentGcnWebRequest): CheckStudentGcnResponse {
        val request = CheckStudentGcnRequest(
            schoolId = webRequest.schoolId,
            grade = webRequest.grade,
            classRoom = webRequest.classRoom,
            number = webRequest.number
        )

        val result = checkStudentGcnUseCase.execute(request)

        return CheckStudentGcnResponse(result)
    }
}
   
