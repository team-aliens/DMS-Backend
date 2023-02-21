package team.aliens.dms.persistence.remain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.spi.RemainAvailableTimePort
import team.aliens.dms.persistence.remain.mapper.RemainAvailableTimeMapper
import team.aliens.dms.persistence.remain.repository.RemainAvailableTimeJpaRepository
import java.util.UUID

@Component
class RemainAvailableTimePersistenceAdapter(
    private val remainAvailableTimeJpaRepository: RemainAvailableTimeJpaRepository,
    private val remainAvailableTimeMapper: RemainAvailableTimeMapper
) : RemainAvailableTimePort {

    override fun queryRemainAvailableTimeBySchoolId(schoolId: UUID) = remainAvailableTimeMapper.toDomain(
        remainAvailableTimeJpaRepository.findByIdOrNull(schoolId)
    )

}