package team.aliens.dms.persistence.volunteer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.VolunteerApplicationPort
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import java.util.UUID

@Component
class VolunteerApplicationPersistenceAdapter(
    private val volunteerApplicationMapper: VolunteerApplicationMapper,
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository,
) : VolunteerApplicationPort {

    override fun queryVolunteerApplicationById(volunteerApplicationId: UUID) =
        volunteerApplicationMapper.toDomain(
            volunteerApplicationRepository.findByIdOrNull(volunteerApplicationId)
        )

    override fun saveVolunteerApplication(volunteerApplication: VolunteerApplication) =
        volunteerApplicationMapper.toDomain(
            volunteerApplicationRepository.save(
                volunteerApplicationMapper.toEntity(volunteerApplication)
            )
        )!!

    override fun deleteVolunteerApplication(volunteerApplication: VolunteerApplication) {
        volunteerApplicationRepository.delete(
            volunteerApplicationMapper.toEntity(volunteerApplication)
        )
    }

    override fun queryVolunteerApplicationsByStudentId(studentId: UUID) =
        volunteerApplicationRepository.findByStudentId(studentId)
            .mapNotNull { volunteerApplicationMapper.toDomain(it) }
}
