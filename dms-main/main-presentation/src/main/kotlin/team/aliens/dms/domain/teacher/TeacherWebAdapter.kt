package team.aliens.dms.domain.teacher

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.teacher.dto.response.TeachersResponse
import team.aliens.dms.domain.teacher.usecase.QueryGeneralTeacherUseCase

@Validated
@RequestMapping("/teachers")
@RestController
class TeacherWebAdapter(
    private val queryGeneralTeacherUseCase: QueryGeneralTeacherUseCase
){

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    fun queryGeneralTeachers(): TeachersResponse {
        return queryGeneralTeacherUseCase.execute()
    }
}