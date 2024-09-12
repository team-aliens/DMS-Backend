package team.aliens.dms.persistence.volunteer

import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.spi.VolunteerPort
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class VolunteerPersistenceAdapter(
    private val volunteerMapper: VolunteerMapper,
    private val volunteerJpaRepository: VolunteerJpaRepository
) : VolunteerPort {

    override fun saveVolunteer(volunteer: Volunteer): Volunteer = volunteerMapper.toDomain(
        volunteerJpaRepository.save(
            volunteerMapper.toEntity(volunteer)
        )
    )!!

    override fun queryVolunteerById(volunteerId: UUID): Volunteer? = volunteerMapper.toDomain(
        volunteerJpaRepository.findById(volunteerId).getOrNull()
    )
}
