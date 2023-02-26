package team.aliens.dms.domain.school

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.dto.request.UpdateQuestionWebRequest
import team.aliens.dms.domain.school.dto.response.ReissueSchoolCodeResponse
import team.aliens.dms.domain.school.dto.response.SchoolIdResponse
import team.aliens.dms.domain.school.dto.response.SchoolQuestionResponse
import team.aliens.dms.domain.school.usecase.CheckSchoolAnswerUseCase
import team.aliens.dms.domain.school.usecase.CheckSchoolCodeUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolQuestionUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolsUseCase
import team.aliens.dms.domain.school.usecase.ReissueSchoolCodeUseCase
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
    private val reissueSchoolCodeUseCase: ReissueSchoolCodeUseCase
) {

    @GetMapping
    fun getSchools(): SchoolsResponse {
        return querySchoolsUseCase.execute()
    }

    @GetMapping("/question/{school-id}")
    fun getQuestion(@PathVariable("school-id") @NotNull schoolId: UUID?): SchoolQuestionResponse {
        val result = querySchoolQuestionUseCase.execute(schoolId!!)

        return SchoolQuestionResponse(result)
    }

    @GetMapping("/answer/{school-id}")
    fun checkAnswer(
        @PathVariable("school-id") @NotNull schoolId: UUID?,
        @RequestParam @NotBlank answer: String?
    ) {
        checkSchoolAnswerUseCase.execute(
            schoolId = schoolId!!,
            answer = answer!!
        )
    }

    @GetMapping("/code")
    fun checkCode(
        @RequestParam("school_code") @NotBlank @Size(max = 8) schoolCode: String?
    ): SchoolIdResponse {
        val result = checkSchoolCodeUseCase.execute(schoolCode!!)

        return SchoolIdResponse(result)
    }

    @PatchMapping("/code")
    fun reissueCode(): ReissueSchoolCodeResponse {
        val result = reissueSchoolCodeUseCase.execute()

        return ReissueSchoolCodeResponse(result)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/question")
    fun updateQuestion(@RequestBody @Valid webRequest: UpdateQuestionWebRequest) {
        val request = UpdateQuestionRequest(
            question = webRequest.question!!,
            answer = webRequest.answer!!
        )

        updateQuestionUseCase.execute(request)
    }
}
