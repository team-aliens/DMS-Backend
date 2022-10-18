package team.aliens.dms.student

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.student.usecase.CheckEmailDuplicateUseCase
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/students")
@RestController
class StudentWebAdapter(
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase
) {

    @GetMapping("/email/duplication")
    fun checkDuplicateEmail(@RequestParam @NotBlank email: String) {
        checkEmailDuplicateUseCase.execute(email)
    }
}