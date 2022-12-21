package team.aliens.dms.persistence.studyroom

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.spi.AvailableTimePort
import team.aliens.dms.persistence.studyroom.mapper.AvailableTimeMapper
import team.aliens.dms.persistence.studyroom.repository.AvailableTimeJpaRepository
import java.util.UUID

@Component
class AvailableTimePersistenceAdapter(
    private val availableTimeMapper: AvailableTimeMapper,
    private val availableTimeRepository: AvailableTimeJpaRepository
) : AvailableTimePort {

    override fun queryAvailableTimeBySchoolId(schoolId: UUID) = availableTimeMapper.toDomain(
        availableTimeRepository.findByIdOrNull(schoolId)
    )

    override fun saveAvailableTime(availableTime: AvailableTime) = availableTimeMapper.toDomain(
        availableTimeRepository.save(
            availableTimeMapper.toEntity(availableTime)
        )
    )!!
}