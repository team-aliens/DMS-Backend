package team.aliens.dms.domain.daybreak

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.dto.ApplyDaybreakStudyApplicationWebRequest
import team.aliens.dms.domain.daybreak.dto.request.ApplyDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyTypesResponse
import team.aliens.dms.domain.daybreak.dto.response.ManagerDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.dto.response.TeacherDaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.usecase.ApplyDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryDaybreakStudyTypesUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryManagerDaybreakStudyApplicationUseCase
import team.aliens.dms.domain.daybreak.usecase.QueryTeacherDaybreakStudyApplicationUseCase
import java.util.UUID

@Validated
@RestController
@RequestMapping("/daybreaks")
class DaybreakWebAdapter(
    private val applyDaybreakStudyApplicationUseCase: ApplyDaybreakStudyApplicationUseCase,
    private val queryTeacherDaybreakStudyApplicationUsecase: QueryTeacherDaybreakStudyApplicationUseCase,
    private val queryManagerDaybreakStudyApplicationUseCase: QueryManagerDaybreakStudyApplicationUseCase,
    private val queryDaybreakStudyTypesUseCase: QueryDaybreakStudyTypesUseCase
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
    @GetMapping("/teacher/study-application")
    fun getDaybreakStudyApplications(
        @RequestParam(value = "type_id", required = false) typeId: UUID?,
        @RequestParam(value = "status", required = false) status: Status?,
        @ModelAttribute pageData: PageData
    ): TeacherDaybreakStudyApplicationResponse {
        return queryTeacherDaybreakStudyApplicationUsecase.execute(typeId, status, pageData)
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
    fun getDaybreakStudyTypes() : DaybreakStudyTypesResponse {
        return queryDaybreakStudyTypesUseCase.execute()
    }
}
