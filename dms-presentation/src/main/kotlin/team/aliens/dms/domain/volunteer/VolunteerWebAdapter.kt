package team.aliens.dms.domain.volunteer

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerRequest
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerWebRequest
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerRequest
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerWebRequest
import team.aliens.dms.domain.volunteer.dto.response.AvailableVolunteersResponse
import team.aliens.dms.domain.volunteer.dto.response.CurrentVolunteerApplicantsResponse
import team.aliens.dms.domain.volunteer.dto.response.QueryMyVolunteerApplicationResponse
import team.aliens.dms.domain.volunteer.dto.response.VolunteerApplicantsResponse
import team.aliens.dms.domain.volunteer.dto.response.VolunteersResponse
import team.aliens.dms.domain.volunteer.usecase.ApplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.ApproveVolunteerApplicationUseCase
import team.aliens.dms.domain.volunteer.usecase.CreateVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.DeleteVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.ExcludeVolunteerApplicationUseCase
import team.aliens.dms.domain.volunteer.usecase.ManagerGetAllVolunteersUseCase
import team.aliens.dms.domain.volunteer.usecase.QueryAppliedStudentUseCase
import team.aliens.dms.domain.volunteer.usecase.QueryAvailableVolunteersUseCase
import team.aliens.dms.domain.volunteer.usecase.QueryCurrentVolunteerApplicantsUseCase
import team.aliens.dms.domain.volunteer.usecase.QueryMyVolunteerApplicationUseCase
import team.aliens.dms.domain.volunteer.usecase.RejectVolunteerApplicationUseCase
import team.aliens.dms.domain.volunteer.usecase.UnapplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.UpdateVolunteerUseCase
import java.util.UUID

@Validated
@RequestMapping("/volunteers")
@RestController
class VolunteerWebAdapter(
    private val applyVolunteerUseCase: ApplyVolunteerUseCase,
    private val unapplyVolunteerUseCase: UnapplyVolunteerUseCase,
    private val createVolunteerUseCase: CreateVolunteerUseCase,
    private val updateVolunteerUseCase: UpdateVolunteerUseCase,
    private val deleteVolunteerUseCase: DeleteVolunteerUseCase,
    private val approveVolunteerApplicationUseCase: ApproveVolunteerApplicationUseCase,
    private val rejectVolunteerApplicationUseCase: RejectVolunteerApplicationUseCase,
    private val queryAvailableVolunteersUseCase: QueryAvailableVolunteersUseCase,
    private val queryMyVolunteerApplicationUseCase: QueryMyVolunteerApplicationUseCase,
    private val managerGetAllVolunteersUseCase: ManagerGetAllVolunteersUseCase,
    private val queryAppliedStudentUseCase: QueryAppliedStudentUseCase,
    private val queryCurrentVolunteerApplicantsUseCase: QueryCurrentVolunteerApplicantsUseCase,
    private val excludeVolunteerApplicationUseCase: ExcludeVolunteerApplicationUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/application/{volunteer-application-id}")
    fun applyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        applyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cancellation/{volunteer-application-id}")
    fun unapplyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        unapplyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my/application")
    fun getMyVolunteerApplications(): QueryMyVolunteerApplicationResponse {
        return queryMyVolunteerApplicationUseCase.execute()
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun getAvailableVolunteers(): AvailableVolunteersResponse {
        return queryAvailableVolunteersUseCase.execute()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createVolunteer(@Valid @RequestBody createVolunteerWebRequest: CreateVolunteerWebRequest) {
        createVolunteerUseCase.execute(
            CreateVolunteerRequest(
                name = createVolunteerWebRequest.name,
                content = createVolunteerWebRequest.content,
                availableGrade = createVolunteerWebRequest.availableGrade,
                availableSex = createVolunteerWebRequest.availableSex,
                score = createVolunteerWebRequest.score!!,
                optionalScore = createVolunteerWebRequest.optionalScore!!,
                maxApplicants = createVolunteerWebRequest.maxApplicants!!,
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{volunteer-id}")
    fun updateVolunteer(
        @Valid @RequestBody updateVolunteerWebRequest: UpdateVolunteerWebRequest,
        @PathVariable("volunteer-id") @NotNull volunteerId: UUID
    ) {
        updateVolunteerUseCase.execute(
            UpdateVolunteerRequest(
                name = updateVolunteerWebRequest.name,
                content = updateVolunteerWebRequest.content,
                availableGrade = updateVolunteerWebRequest.availableGrade,
                availableSex = updateVolunteerWebRequest.availableSex,
                score = updateVolunteerWebRequest.score!!,
                optionalScore = updateVolunteerWebRequest.optionalScore!!,
                maxApplicants = updateVolunteerWebRequest.maxApplicants!!,
                volunteerId = volunteerId
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{volunteer-id}")
    fun deleteVolunteer(@PathVariable("volunteer-id") @NotNull volunteerId: UUID) {
        deleteVolunteerUseCase.execute(volunteerId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/approval/{volunteer-application-id}")
    fun approveVolunteerApplication(@PathVariable("volunteer-application-id") volunteerApplicationId: UUID) {
        approveVolunteerApplicationUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/rejection/{volunteer-application-id}")
    fun rejectVolunteerApplication(@PathVariable("volunteer-application-id") volunteerApplicationId: UUID) {
        rejectVolunteerApplicationUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/exception/{volunteer-application-id}")
    fun excludeVolunteerApplication(@PathVariable("volunteer-application-id") volunteerApplicationId: UUID) {
        excludeVolunteerApplicationUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manager")
    fun managerGetAllVolunteers(): VolunteersResponse {
        return managerGetAllVolunteersUseCase.execute()
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{volunteer-id}")
    fun queryAppliedStudent(@PathVariable("volunteer-id") @NotNull volunteerId: UUID): VolunteerApplicantsResponse {
        return queryAppliedStudentUseCase.execute(volunteerId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/current")
    fun queryAppliedStudent(): CurrentVolunteerApplicantsResponse {
        return queryCurrentVolunteerApplicantsUseCase.execute()
    }
}
