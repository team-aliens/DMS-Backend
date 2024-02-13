package team.aliens.dms.domain.outing

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.outing.dto.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.ApplyOutingResponse
import team.aliens.dms.domain.outing.dto.CreateOutingTypeRequest
import team.aliens.dms.domain.outing.dto.request.ApplyOutingWebRequest
import team.aliens.dms.domain.outing.dto.request.CreateOutingTypeWebRequest
import team.aliens.dms.domain.outing.usecase.ApplyOutingUseCase
import team.aliens.dms.domain.outing.usecase.CreateOutingTypeUseCase
import team.aliens.dms.domain.outing.usecase.RemoveOutingTypeUseCase

@Validated
@RequestMapping("/outings")
@RestController
class OutingWebAdapter(
    private val applyOutingUseCase: ApplyOutingUseCase,
    private val createOutingTypeUseCase: CreateOutingTypeUseCase,
    private val removeOutingTypeUseCase: RemoveOutingTypeUseCase
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
                outingTypeTitle = webRequest.outingTypeTitle,
                reason = webRequest.reason,
                companionIds = webRequest.companionIds
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/types")
    fun createOutingType(@RequestBody @Valid request: CreateOutingTypeWebRequest) {
        createOutingTypeUseCase.execute(
            CreateOutingTypeRequest(request.title)
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/types/{title}")
    fun removeOutingType(@PathVariable title: String) {
        removeOutingTypeUseCase.execute(title)
    }
}
