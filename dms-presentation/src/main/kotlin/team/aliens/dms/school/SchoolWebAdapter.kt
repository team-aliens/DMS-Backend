package team.aliens.dms.school

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.usecase.CheckSchoolAnswerUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolQuestionUseCase
import team.aliens.dms.domain.school.usecase.QuerySchoolsUseCase
import team.aliens.dms.school.dto.response.SchoolQuestionResponse
import java.util.*
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/schools")
@RestController
class SchoolWebAdapter(
    private val querySchoolsUseCase: QuerySchoolsUseCase,
    private val querySchoolQuestionUseCase: QuerySchoolQuestionUseCase,
    private val checkSchoolAnswerUseCase: CheckSchoolAnswerUseCase
) {

    @GetMapping
    fun getSchools(): SchoolsResponse {
        return querySchoolsUseCase.execute()
    }

    @GetMapping("/question/{school-id}")
    fun getQuestion(@PathVariable("school-id") schoolId: UUID): SchoolQuestionResponse {
        val result = querySchoolQuestionUseCase.execute(schoolId)

        return SchoolQuestionResponse(result)
    }

    @GetMapping("/answer/{school-id}")
    fun checkAnswer(
        @PathVariable("school-id") schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ) {
        checkSchoolAnswerUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )
    }

}