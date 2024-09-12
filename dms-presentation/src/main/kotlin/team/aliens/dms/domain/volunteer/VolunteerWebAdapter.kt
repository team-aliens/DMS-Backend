package team.aliens.dms.domain.volunteer

import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.volunteer.usecase.ApplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.UnapplyVolunteerUseCase
import java.util.UUID

@Validated
@RequestMapping("/volunteers")
@RestController
class VolunteerWebAdapter(
    private val applyVolunteerUseCase: ApplyVolunteerUseCase,
    private val unapplyVolunteerUseCase: UnapplyVolunteerUseCase
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
}
