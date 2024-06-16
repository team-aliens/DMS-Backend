package team.aliens.dms.domain.outing

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
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
import team.aliens.dms.common.extension.setExcelContentDisposition
import team.aliens.dms.domain.outing.dto.request.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.request.ApplyOutingWebRequest
import team.aliens.dms.domain.outing.dto.request.CreateOutingTypeRequest
import team.aliens.dms.domain.outing.dto.request.CreateOutingTypeWebRequest
import team.aliens.dms.domain.outing.dto.request.SetOutingAvailableTimeRequest
import team.aliens.dms.domain.outing.dto.request.SetOutingAvailableTimeWebRequest
import team.aliens.dms.domain.outing.dto.request.UpdateOutingAvailableTimeWebRequest
import team.aliens.dms.domain.outing.dto.response.ApplyOutingResponse
import team.aliens.dms.domain.outing.dto.response.GetAllOutingTypeTitlesResponse
import team.aliens.dms.domain.outing.dto.response.GetCurrentOutingApplicationResponse
import team.aliens.dms.domain.outing.dto.response.OutingApplicationHistoriesResponse
import team.aliens.dms.domain.outing.dto.response.OutingAvailableTimesResponse
import team.aliens.dms.domain.outing.dto.response.OutingHistoryDetailsResponse
import team.aliens.dms.domain.outing.dto.response.SetOutingAvailableTimeResponse
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.usecase.ApplyOutingUseCase
import team.aliens.dms.domain.outing.usecase.CreateOutingTypeUseCase
import team.aliens.dms.domain.outing.usecase.ExportAllOutingApplicationsUseCase
import team.aliens.dms.domain.outing.usecase.GetAllOutingTypeTitlesUseCase
import team.aliens.dms.domain.outing.usecase.GetCurrentOutingApplicationUseCase
import team.aliens.dms.domain.outing.usecase.GetOutingApplicationHistoriesUseCase
import team.aliens.dms.domain.outing.usecase.GetOutingAvailableTimesUseCase
import team.aliens.dms.domain.outing.usecase.GetOutingHistoryDetailsUseCase
import team.aliens.dms.domain.outing.usecase.RemoveOutingAvailableTimeUseCase
import team.aliens.dms.domain.outing.usecase.RemoveOutingTypeUseCase
import team.aliens.dms.domain.outing.usecase.SetOutingAvailableTimeUseCase
import team.aliens.dms.domain.outing.usecase.ToggleOutingAvailableTimeUseCase
import team.aliens.dms.domain.outing.usecase.UnApplyOutingUseCase
import team.aliens.dms.domain.outing.usecase.UpdateOutingAvailableTimeUseCase
import team.aliens.dms.domain.outing.usecase.UpdateOutingStatusUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Validated
@RequestMapping("/outings")
@RestController
class OutingWebAdapter(
    private val applyOutingUseCase: ApplyOutingUseCase,
    private val createOutingTypeUseCase: CreateOutingTypeUseCase,
    private val removeOutingTypeUseCase: RemoveOutingTypeUseCase,
    private val unApplyOutingUseCase: UnApplyOutingUseCase,
    private val getAllOutingTypeTitlesUseCase: GetAllOutingTypeTitlesUseCase,
    private val updateOutingStatusUseCase: UpdateOutingStatusUseCase,
    private val exportAllOutingApplicationsUseCase: ExportAllOutingApplicationsUseCase,
    private val getCurrentOutingApplicationUseCase: GetCurrentOutingApplicationUseCase,
    private val getOutingApplicationHistoriesUseCase: GetOutingApplicationHistoriesUseCase,
    private val getOutingAvailableTimesUseCase: GetOutingAvailableTimesUseCase,
    private val getOutingHistoryDetailsUseCase: GetOutingHistoryDetailsUseCase,
    private val setOutingAvailableTimeUseCase: SetOutingAvailableTimeUseCase,
    private val removeOutingAvailableTimeUseCase: RemoveOutingAvailableTimeUseCase,
    private val updateOutingAvailableTimeUseCase: UpdateOutingAvailableTimeUseCase,
    private val toggleOutingAvailableTimeUseCase: ToggleOutingAvailableTimeUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun applyOuting(@RequestBody @Valid webRequest: ApplyOutingWebRequest): ApplyOutingResponse {
        return applyOutingUseCase.execute(
            ApplyOutingRequest(
                outingDate = webRequest.outingDate,
                outingTime = webRequest.outingTime,
                arrivalTime = webRequest.arrivalTime,
                outingTypeTitle = webRequest.outingTypeTitle,
                reason = webRequest.reason,
                companionIds = webRequest.companionIds
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{outing-application-id}")
    fun unApplyOuting(@PathVariable("outing-application-id") @NotNull outingApplicationId: UUID) {
        unApplyOutingUseCase.execute(outingApplicationId)
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

    @GetMapping("/files")
    fun exportAllOutingApplications(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: LocalDate,
        httpResponse: HttpServletResponse
    ): ByteArray {
        val response = exportAllOutingApplicationsUseCase.execute(start, end)
        httpResponse.setExcelContentDisposition(response.fileName)
        return response.file
    }

    @GetMapping("/my")
    fun getCurrentOutingApplication(): GetCurrentOutingApplicationResponse {
        return getCurrentOutingApplicationUseCase.execute()
    }

    @GetMapping("/histories")
    fun getOutingApplicationHistories(
        @RequestParam(name = "student_name", required = false) studentName: String?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): OutingApplicationHistoriesResponse {
        return getOutingApplicationHistoriesUseCase.execute(
            studentName = studentName,
            date = date
        )
    }

    @GetMapping("/available-time")
    fun getOutingAvailableTime(
        @RequestParam dayOfWeek: DayOfWeek
    ): OutingAvailableTimesResponse {
        return getOutingAvailableTimesUseCase.execute(dayOfWeek)
    }

    @GetMapping("/history/{outing-application-id}")
    fun getOutingHistoryDetails(
        @PathVariable("outing-application-id") @NotNull outingApplicationId: UUID
    ): OutingHistoryDetailsResponse {
        return getOutingHistoryDetailsUseCase.execute(outingApplicationId)
    }

    @PostMapping("/available-time")
    fun setOutingAvailableTime(
        @RequestBody @Valid webRequest: SetOutingAvailableTimeWebRequest
    ): SetOutingAvailableTimeResponse {
        return setOutingAvailableTimeUseCase.execute(
            SetOutingAvailableTimeRequest(
                dayOfWeek = webRequest.dayOfWeek,
                startTime = webRequest.startTime,
                endTime = webRequest.endTime
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/available-time/{outing-available-time-id}")
    fun removeOutingAvailableTime(@PathVariable("outing-available-time-id") @NotNull outingAvailableTimeId: UUID) {
        removeOutingAvailableTimeUseCase.execute(outingAvailableTimeId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/available-time/{outing-available-time-id}")
    fun updateOutingAvailableTime(
        @PathVariable("outing-available-time-id") @NotNull outingAvailableTimeId: UUID,
        @RequestBody @Valid request: UpdateOutingAvailableTimeWebRequest
    ) {
        updateOutingAvailableTimeUseCase.execute(
            outingAvailableTimeId = outingAvailableTimeId,
            outingTime = request.outingTime,
            arrivalTime = request.arrivalTime
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/available-time/toggle/{outing-available-time-id}")
    fun toggleOutingAvailableTime(@PathVariable("outing-available-time-id") @NotNull outingAvailableTimeId: UUID) {
        toggleOutingAvailableTimeUseCase.execute(outingAvailableTimeId)
    }
}
