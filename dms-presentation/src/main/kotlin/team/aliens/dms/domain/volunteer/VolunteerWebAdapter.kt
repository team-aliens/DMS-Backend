package team.aliens.dms.domain.volunteer

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import team.aliens.dms.domain.volunteer.dto.CreateVolunteerRequest
import team.aliens.dms.domain.volunteer.dto.CreateVolunteerWebRequest
import team.aliens.dms.domain.volunteer.usecase.ApplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.UnapplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.CreateVolunteerUseCase
import java.util.UUID

@Validated
@RequestMapping("/volunteers")
@RestController
class VolunteerWebAdapter(
    private val applyVolunteerUseCase: ApplyVolunteerUseCase,
    private val unapplyVolunteerUseCase: UnapplyVolunteerUseCase,
    private val createVolunteerUseCase: CreateVolunteerUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{volunteer-application-id}")
    fun applyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        applyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{volunteer-application-id}")
    fun unapplyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        unapplyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @PostMapping
    fun create(@Valid @RequestBody createVolunteerWebRequest: CreateVolunteerWebRequest) {
        createVolunteerUseCase.execute(
            CreateVolunteerRequest(
                name = createVolunteerWebRequest.name,
                content = createVolunteerWebRequest.content,
                gradeCondition = createVolunteerWebRequest.gradeCondition,
                sexCondition = createVolunteerWebRequest.sexCondition,
                score = createVolunteerWebRequest.score,
                optionalScore = createVolunteerWebRequest.optionalScore,
                maxApplicants = createVolunteerWebRequest.maxApplicants,
            )
        )
    }
}
