package team.aliens.dms.school

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.school.dto.SchoolsResponse
import team.aliens.dms.domain.school.usecase.QuerySchoolsUseCase

@RestController
@RequestMapping("/schools")
class SchoolController(
    private val querySchoolsUseCase: QuerySchoolsUseCase
) {

    @GetMapping
    fun getSchools(): SchoolsResponse {
        return querySchoolsUseCase.execute()
    }

}