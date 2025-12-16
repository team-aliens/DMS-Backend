package team.aliens.dms.domain.volunteer.dto.request

import jakarta.validation.constraints.NotNull

data class UpdateVolunteerScoreRequest(

    @field:NotNull
    val updateScore: Int
)
