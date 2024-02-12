package team.aliens.dms.domain.outing

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.outing.dto.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.ApplyOutingResponse
import team.aliens.dms.domain.outing.dto.request.ApplyOutingWebRequest
import team.aliens.dms.domain.outing.usecase.ApplyOutingUseCase

@Validated
@RequestMapping("/outings")
@RestController
class OutingWebAdapter(
    private val applyOutingUseCase: ApplyOutingUseCase,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun applyOuting(@RequestBody @Valid webRequest: ApplyOutingWebRequest): ApplyOutingResponse {
        return applyOutingUseCase.execute(
            ApplyOutingRequest(
                outAt = webRequest.outAt,
                outingTime = webRequest.outingTime,
                arrivalTime = webRequest.arrivalTime,
                destination = webRequest.destination,
                outingTypeId = webRequest.outingTypeId,
                reason = webRequest.reason,
                companionIds = webRequest.companionIds
            )
        )
    }
}
