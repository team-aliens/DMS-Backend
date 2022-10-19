package team.aliens.dms.manager

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.manager.usecase.FindAccountIdUseCase
import team.aliens.dms.manager.dto.response.FindAccountIdResponse
import java.util.*
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/managers")
@RestController
class ManagerWebAdapter(
    private val findAccountIdUseCase: FindAccountIdUseCase
) {

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ): FindAccountIdResponse {
        val result = findAccountIdUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )

        return FindAccountIdResponse(result);
    }
}