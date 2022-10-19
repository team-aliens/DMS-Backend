package team.aliens.dms.student

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.student.usecase.CheckDuplicatedAccountIdUseCase
import team.aliens.dms.domain.student.usecase.CheckDuplicatedEmailUseCase
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/students")
@RestController
class StudentWebAdapter(
    private val checkDuplicatedEmailUseCase: CheckDuplicatedEmailUseCase,
    private val checkDuplicatedAccountIdUseCase: CheckDuplicatedAccountIdUseCase
) {

    @GetMapping("/email/duplication")
    fun checkDuplicatedEmail(@RequestParam @NotBlank email: String) {
        checkDuplicatedEmailUseCase.execute(email)
    }

    @GetMapping("/account-id/duplication")
    fun checkDuplicatedAccountId(@RequestParam @NotBlank accountId: String) {
        checkDuplicatedAccountIdUseCase.execute(accountId)
    }
}