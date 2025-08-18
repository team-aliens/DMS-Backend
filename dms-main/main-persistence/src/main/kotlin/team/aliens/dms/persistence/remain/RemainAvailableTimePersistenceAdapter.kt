package team.aliens.dms.persistence.remain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.spi.RemainAvailableTimePort
import team.aliens.dms.persistence.remain.mapper.RemainAvailableTimeMapper
import team.aliens.dms.persistence.remain.repository.RemainAvailableTimeJpaRepository
import java.util.UUID

@Component
class RemainAvailableTimePersistenceAdapter(
    private val remainAvailableTimeRepository: RemainAvailableTimeJpaRepository,
    private val remainAvailableTimeMapper: RemainAvailableTimeMapper
) : RemainAvailableTimePort {

    override fun saveRemainAvailableTime(remainAvailableTime: RemainAvailableTime) = remainAvailableTimeMapper.toDomain(
        remainAvailableTimeRepository.save(
            remainAvailableTimeMapper.toEntity(remainAvailableTime)
        )
    )!!

    override fun queryRemainAvailableTimeBySchoolId(schoolId: UUID) = remainAvailableTimeMapper.toDomain(
        remainAvailableTimeRepository.findByIdOrNull(schoolId)
    )
}
