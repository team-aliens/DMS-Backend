package team.aliens.dms.domain.outing

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.outing.dto.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.ApplyOutingResponse
import team.aliens.dms.domain.outing.dto.CreateOutingTypeRequest
import team.aliens.dms.domain.outing.dto.GetAllOutingTypeTitlesResponse
import team.aliens.dms.domain.outing.dto.request.ApplyOutingWebRequest
import team.aliens.dms.domain.outing.dto.request.CreateOutingTypeWebRequest
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.usecase.ApplyOutingUseCase
import team.aliens.dms.domain.outing.usecase.CreateOutingTypeUseCase
import team.aliens.dms.domain.outing.usecase.GetAllOutingTypeTitlesUseCase
import team.aliens.dms.domain.outing.usecase.RemoveOutingTypeUseCase
import team.aliens.dms.domain.outing.usecase.UpdateOutingStatusUseCase
import java.util.UUID

@Validated
@RequestMapping("/outings")
@RestController
class OutingWebAdapter(
    private val applyOutingUseCase: ApplyOutingUseCase,
    private val createOutingTypeUseCase: CreateOutingTypeUseCase,
    private val removeOutingTypeUseCase: RemoveOutingTypeUseCase,
    private val getAllOutingTypeTitlesUseCase: GetAllOutingTypeTitlesUseCase,
    private val updateOutingStatusUseCase: UpdateOutingStatusUseCase
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

    @GetMapping("/types")
    fun getAllOutingTypeTitles(@RequestParam(required = false) keyword: String?): GetAllOutingTypeTitlesResponse {
        return getAllOutingTypeTitlesUseCase.execute(keyword)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{outing-application-id}")
    fun updateOutingStatus(
        @PathVariable("outing-application-id") @NotNull outingApplicationId: UUID,
        @RequestParam("outing_status") @NotNull outingStatus: OutingStatus
    ) {
        updateOutingStatusUseCase.execute(outingApplicationId, outingStatus)
    }
}
