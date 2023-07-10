package team.aliens.dms.domain.school

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.school.dto.ApplicationAvailableTimeResponse
import team.aliens.dms.domain.school.dto.AvailableFeaturesResponse
import team.aliens.dms.domain.school.dto.ReissueSchoolCodeResponse
import team.aliens.dms.domain.school.dto.SchoolIdResponse
import team.aliens.dms.domain.school.dto.SchoolQuestionResponse
import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.dto.UpdateApplicationAvailableTimeRequest
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.dto.request.UpdateApplicationAvailableTimeWebRequest
import team.aliens.dms.domain.school.dto.request.UpdateQuestionWebRequest
import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.usecase.CheckSchoolAnswerUseCase
import team.aliens.dms.domain.school.usecase.CheckSchoolCodeUseCase
import team.aliens.dms.domain.school.usecase.QueryApplicationAvailableTimeUseCase
import team.aliens.dms.domain.school.usecase.QueryAvailableFeaturesUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolQuestionUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolsUseCase
import team.aliens.dms.domain.school.usecase.ReissueSchoolCodeUseCase
import team.aliens.dms.domain.school.usecase.UpdateApplicationAvailableTimeUseCase
import team.aliens.dms.domain.school.usecase.UpdateQuestionUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Validated
@RequestMapping("/schools")
@RestController
class SchoolWebAdapter(
    private val querySchoolsUseCase: QuerySchoolsUseCase,
    private val querySchoolQuestionUseCase: QuerySchoolQuestionUseCase,
    private val checkSchoolAnswerUseCase: CheckSchoolAnswerUseCase,
    private val checkSchoolCodeUseCase: CheckSchoolCodeUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase,
    private val reissueSchoolCodeUseCase: ReissueSchoolCodeUseCase,
    private val queryAvailableFeaturesUseCase: QueryAvailableFeaturesUseCase,
    private val updateApplicationAvailableTimeUseCase: UpdateApplicationAvailableTimeUseCase,
    private val queryApplicationAvailableTimeUseCase: QueryApplicationAvailableTimeUseCase
) {

    @GetMapping
    fun getSchools(): SchoolsResponse {
        return querySchoolsUseCase.execute()
    }

    @GetMapping("/question/{school-id}")
    fun getQuestion(@PathVariable("school-id") @NotNull schoolId: UUID): SchoolQuestionResponse {
        return querySchoolQuestionUseCase.execute(schoolId)
    }

    @GetMapping("/answer/{school-id}")
    fun checkAnswer(
        @PathVariable("school-id") @NotNull schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ) {
        checkSchoolAnswerUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )
    }

    @GetMapping("/code")
    fun checkCode(
        @RequestParam("school_code") @NotBlank @Size(max = 8) schoolCode: String
    ): SchoolIdResponse {
        return checkSchoolCodeUseCase.execute(schoolCode)
    }

    @GetMapping("/available-features")
    fun queryAvailableFeatures(): AvailableFeaturesResponse {
        return queryAvailableFeaturesUseCase.execute()
    }

    @PatchMapping("/code")
    fun reissueCode(): ReissueSchoolCodeResponse {
        return reissueSchoolCodeUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/question")
    fun updateQuestion(@RequestBody @Valid webRequest: UpdateQuestionWebRequest) {
        val request = UpdateQuestionRequest(
            question = webRequest.question,
            answer = webRequest.answer
        )

        updateQuestionUseCase.execute(request)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/available-time")
    fun updateApplicationAvailableTime(@RequestBody @Valid webRequest: UpdateApplicationAvailableTimeWebRequest) {
        val request = UpdateApplicationAvailableTimeRequest(
            startDayOfWeek = webRequest.startDayOfWeek!!,
            startTime = webRequest.startTime,
            endDayOfWeek = webRequest.endDayOfWeek!!,
            endTime = webRequest.endTime,
            type = webRequest.type
        )

        updateApplicationAvailableTimeUseCase.execute(request)
    }

    @GetMapping("/available-time")
    fun getApplicationAvailableTime(@RequestParam type: ApplicationAvailableTimeType): ApplicationAvailableTimeResponse {
        return queryApplicationAvailableTimeUseCase.execute(type)
    }
}
