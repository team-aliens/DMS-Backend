package team.aliens.dms.domain.daybreak

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.dto.request.ApplyDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.dto.request.ApplyDaybreakStudyApplicationWebRequest
import team.aliens.dms.domain.daybreak.dto.request.ChangeDaybreakStudyApplicationStatusRequest
import team.aliens.dms.domain.daybreak.dto.request.ChangeDaybreakStudyApplicationStatusWebRequest
import team.aliens.dms.domain.daybreak.dto.request.CreateDaybreakStudyTypeRequest
import team.aliens.dms.domain.daybreak.dto.request.CreateDaybreakStudyTypeWebRequest
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyTypesResponse
import team.aliens.dms.domain.daybreak.dto.response.GeneralTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.dto.response.HeadTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.dto.response.ManagerDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.usecase.ApplyDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.ChangeStatusDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.CreateDaybreakStudyTypeUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryDaybreakStudyTypesUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryGeneralTeacherDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryHeadTeacherDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryManagerDaybreakStudyApplicationUseCase
import java.time.LocalDate
import java.util.UUID

@Validated
@RestController
@RequestMapping("/daybreaks")
class DaybreakWebAdapter(
    private val applyDaybreakStudyApplicationUseCase: ApplyDaybreakStudyApplicationUseCase,
    private val queryGeneralTeacherDaybreakStudyApplicationUseCase: QueryGeneralTeacherDaybreakStudyApplicationUseCase,
    private val queryHeadTeacherDaybreakStudyApplicationUseCase: QueryHeadTeacherDaybreakStudyApplicationUseCase,
    private val queryManagerDaybreakStudyApplicationUseCase: QueryManagerDaybreakStudyApplicationUseCase,
    private val queryDaybreakStudyTypesUseCase: QueryDaybreakStudyTypesUseCase,
    private val changeStatusDaybreakStudyApplicationUseCase: ChangeStatusDaybreakStudyApplicationUseCase,
    private val createDaybreakStudyTypeUseCase: CreateDaybreakStudyTypeUseCase
) {

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/study-application")
    fun applyDaybreakStudyApplication(@RequestBody @Valid request: ApplyDaybreakStudyApplicationWebRequest) {
        applyDaybreakStudyApplicationUseCase.execute(
            ApplyDaybreakStudyApplicationRequest(
                teacherId = request.teacherId,
                typeId = request.typeId,
                reason = request.reason,
                startDate = request.startDate,
                endDate = request.endDate
            )
        )
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/general/study-application")
    fun getDaybreakStudyApplications(
        @RequestParam(value = "type_id", required = false) typeId: UUID?,
        @RequestParam(value = "date", required = true) date: LocalDate,
        @ModelAttribute pageData: PageData
    ): GeneralTeacherDaybreakStudyApplicationsResponse {
        return queryGeneralTeacherDaybreakStudyApplicationUseCase.execute(typeId, date, pageData)
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/head/study-application")
    fun getDaybreakStudyApplications(
        @RequestParam(value = "type_id", required = false) typeId: UUID?,
        @RequestParam(value = "date", required = true) date: LocalDate,
        @RequestParam(value = "status", required = false) status: Status?,
        @ModelAttribute pageData: PageData
    ): HeadTeacherDaybreakStudyApplicationsResponse {
        return queryHeadTeacherDaybreakStudyApplicationUseCase.execute(typeId, date, status, pageData)
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/manager/study-application")
    fun getDaybreakStudyApplications(
        @RequestParam(value = "grade", required = false) grade: Int?,
        @ModelAttribute pageData: PageData
    ): ManagerDaybreakStudyApplicationsResponse {
        return queryManagerDaybreakStudyApplicationUseCase.execute(grade, pageData)
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/study-type")
    fun getDaybreakStudyTypes(): DaybreakStudyTypesResponse {
        return queryDaybreakStudyTypesUseCase.execute()
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PatchMapping("/study-application")
    fun changeDaybreakStudyApplicationStatus(
        @RequestBody @Valid request: ChangeDaybreakStudyApplicationStatusWebRequest
    ) {
        changeStatusDaybreakStudyApplicationUseCase.execute(
            ChangeDaybreakStudyApplicationStatusRequest(
                applicationIds = request.applicationIds,
                status = request.status
            )
        )
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/study-type")
    fun createDaybreakStudyType(@RequestBody @Valid request: CreateDaybreakStudyTypeWebRequest) {
        createDaybreakStudyTypeUseCase.execute(
            CreateDaybreakStudyTypeRequest(
                name = request.name
            )
        )
    }
}
