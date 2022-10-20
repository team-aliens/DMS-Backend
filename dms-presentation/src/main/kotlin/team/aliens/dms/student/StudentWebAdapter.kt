package team.aliens.dms.student

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.dto.ResetPasswordStudentRequest
import team.aliens.dms.domain.student.usecase.CheckDuplicatedAccountIdUseCase
import team.aliens.dms.domain.student.usecase.CheckDuplicatedEmailUseCase
import team.aliens.dms.domain.student.usecase.FindStudentAccountIdUseCase
import team.aliens.dms.domain.student.usecase.ResetPasswordStudentUseCase
import team.aliens.dms.student.dto.request.FindStudentAccountIdWebRequest
import team.aliens.dms.student.dto.request.ResetPasswordStudentWebRequest
import team.aliens.dms.student.dto.response.FindStudentAccountIdResponse
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/students")
@RestController
class StudentWebAdapter(
    private val checkDuplicatedEmailUseCase: CheckDuplicatedEmailUseCase,
    private val checkDuplicatedAccountIdUseCase: CheckDuplicatedAccountIdUseCase,
    private val findStudentAccountIdUseCase: FindStudentAccountIdUseCase,
    private val resetPasswordStudentUseCase: ResetPasswordStudentUseCase
) {

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
    fun resetPassword(@RequestBody @Valid webRequest: ResetPasswordStudentWebRequest) {
        val request = ResetPasswordStudentRequest(
            accountId = webRequest.accountId,
            name = webRequest.name,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword
        )

        resetPasswordStudentUseCase.execute(request)
    }
}