package team.aliens.dms.manager

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.manager.usecase.FindAccountIdUseCase
import team.aliens.dms.user.dto.response.FindAccountIdResponse
import java.util.UUID

@RestController
@RequestMapping("/managers")
class WebManagerAdapter(
    val findAccountIdUseCase: FindAccountIdUseCase
) {

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(@PathVariable("school-id") schoolId: UUID, @RequestParam answer: String) : FindAccountIdResponse {
        return FindAccountIdResponse(findAccountIdUseCase.execute(schoolId, answer));
    }
}