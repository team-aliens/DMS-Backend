package team.aliens.dms.domain.outing

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import team.aliens.dms.domain.outing.dto.CreateOutingApplicationRequest
import team.aliens.dms.domain.outing.dto.CreateOutingApplicationResponse
import team.aliens.dms.domain.outing.dto.request.CreateOutingApplicationWebRequest
import team.aliens.dms.domain.outing.usecase.CreateOutingApplicationUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@RequestMapping("/outings")
@RestController
class OutingWebAdapter(
    private val createOutingUseCase: CreateOutingApplicationUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createOuting(@RequestBody @Valid webRequest: CreateOutingApplicationWebRequest): CreateOutingApplicationResponse {
        return createOutingUseCase.execute(
            CreateOutingApplicationRequest(
                outAt = webRequest.outAt,
                outingTime = webRequest.outingTime,
                arrivalTime = webRequest.arrivalTime,
                destination = webRequest.destination,
                outingTypeId = webRequest.outingTypeId,
                reason = webRequest.reason,
                companionId = webRequest.companionId
            )
        )
    }
}
