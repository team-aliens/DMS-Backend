package team.aliens.dms.manager

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.manager.dto.QueryStudentListResponse
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.usecase.FindManagerAccountIdUseCase
import team.aliens.dms.domain.manager.usecase.GetStudentDetailsUseCase
import team.aliens.dms.domain.manager.usecase.GetStudentListUseCase
import team.aliens.dms.domain.manager.usecase.RemoveStudentUseCase
import team.aliens.dms.domain.manager.usecase.ResetManagerPasswordUseCase
import team.aliens.dms.manager.dto.request.GetStudentListWebRequest
import team.aliens.dms.manager.dto.request.ResetPasswordManagerWebRequest
import team.aliens.dms.manager.dto.response.FindManagerAccountIdResponse
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/managers")
@RestController
class ManagerWebAdapter(
    private val findManagerAccountIdUseCase: FindManagerAccountIdUseCase,
    private val resetManagerPasswordUseCase: ResetManagerPasswordUseCase,
    private val getStudentDetailsUseCase: GetStudentDetailsUseCase,
    private val getStudentListUseCase: GetStudentListUseCase,
    private val removeStudentUseCase: RemoveStudentUseCase
) {

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ): FindManagerAccountIdResponse {
        val result = findManagerAccountIdUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )

        return FindManagerAccountIdResponse(result);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetPasswordManagerWebRequest) {
        val request = ResetManagerPasswordRequest(
            accountId = webRequest.accountId,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword.value
        )

        resetManagerPasswordUseCase.execute(request)
    }

    @GetMapping("/students")
    fun getStudents(@RequestBody @Valid request: GetStudentListWebRequest): QueryStudentListResponse {
        return getStudentListUseCase.execute(
            name = request.name,
            sort = request.sort
        )
    }
    
    @GetMapping("/students/{student-id}")
    fun getStudentDetails(@PathVariable("student-id") studentId: UUID): GetStudentDetailsResponse {
        return getStudentDetailsUseCase.execute(studentId)
    }

    @DeleteMapping("/students/{student-id}")
    fun deleteStudent(@PathVariable("student-id") studentId: UUID) {
        return removeStudentUseCase.execute(studentId)
    }
}