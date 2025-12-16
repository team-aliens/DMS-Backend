package team.aliens.dms.persistence.volunteer

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.VolunteerScorePort
import team.aliens.dms.persistence.volunteer.mapper.VolunteerApplicationMapper
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.mapper.VolunteerScoreMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerApplicationJpaRepository
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository
import team.aliens.dms.persistence.volunteer.repository.VolunteerScoreRepository
import java.util.UUID

@Component
class VolunteerScorePersistenceAdapter(
    private val volunteerScoreRepository: VolunteerScoreRepository,
    private val volunteerApplicationRepository: VolunteerApplicationJpaRepository,
    private val volunteerRepository: VolunteerJpaRepository,
    private val volunteerScoreMapper: VolunteerScoreMapper,
    private val volunteerApplicationMapper: VolunteerApplicationMapper,
    private val volunteerMapper: VolunteerMapper
) : VolunteerScorePort {

    override fun saveVolunteerScore(volunteerScore: VolunteerScore) {
        volunteerScoreRepository.save(
            volunteerScoreMapper.toEntity(volunteerScore)
        )
    }

    override fun updateVolunteerScore(applicationId: UUID, updateScore: Int) {
        val entity = volunteerScoreRepository.findByVolunteerApplicationId(applicationId)
            ?: throw VolunteerApplicationNotFoundException

        entity.assignScore = updateScore

        volunteerScoreRepository.save(entity)
    }

    override fun queryExistsByApplicationId(applicationId: UUID): Boolean {
        return volunteerScoreRepository.existsByVolunteerApplicationId(applicationId)
    }

    override fun queryVolunteerApplicationById(applicationId: UUID): VolunteerApplication? =
        volunteerApplicationRepository.findByIdOrNull(applicationId)
            ?.let { volunteerApplicationMapper.toDomain(it) }

    override fun queryVolunteerById(volunteerId: UUID): Volunteer? =
        volunteerRepository.findByIdOrNull(volunteerId)
            ?.let { volunteerMapper.toDomain(it) }
}
