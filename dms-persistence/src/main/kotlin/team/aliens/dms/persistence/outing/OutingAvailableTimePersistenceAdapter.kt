package team.aliens.dms.persistence.outing

import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.spi.OutingAvailableTimePort
import team.aliens.dms.persistence.outing.mapper.OutingAvailableTimeMapper
import team.aliens.dms.persistence.outing.repository.OutingAvailableTimeJpaRepository
import java.time.DayOfWeek

@Component
class OutingAvailableTimePersistenceAdapter(
    private val outingAvailableTimeRepository: OutingAvailableTimeJpaRepository,
    private val outingAvailableTimeMapper: OutingAvailableTimeMapper
) : OutingAvailableTimePort {

    override fun queryOutingAvailableTimeByDayOfWeek(dayOfWeek: DayOfWeek) =
        outingAvailableTimeMapper.toDomain(
            outingAvailableTimeRepository.findByDayOfWeek(dayOfWeek)
        )
}