package team.aliens.dms.persistence.volunteer.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.volunteer.entity.VolunteerApplicationJpaEntity
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository

@Component
class VolunteerApplicationMapper(
    private val studentRepository: StudentJpaRepository,
    private val volunteerRepository: VolunteerJpaRepository
) : GenericMapper<VolunteerApplication, VolunteerApplicationJpaEntity> {

    override fun toDomain(entity: VolunteerApplicationJpaEntity?): VolunteerApplication? {
        return entity?.let {
            VolunteerApplication(
                id = it.id!!,
                studentId = it.student!!.id!!,
                volunteerId = it.volunteer!!.id!!,
                approved = it.approved
            )
        }
    }

    override fun toEntity(domain: VolunteerApplication): VolunteerApplicationJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val volunteer = volunteerRepository.findByIdOrNull(domain.volunteerId)

        return VolunteerApplicationJpaEntity(
            id = domain.id,
            student = student,
            volunteer = volunteer,
            approved = domain.approved
        )
    }
}
