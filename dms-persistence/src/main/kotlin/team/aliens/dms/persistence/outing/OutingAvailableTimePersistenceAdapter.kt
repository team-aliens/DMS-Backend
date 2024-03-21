package team.aliens.dms.persistence.outing

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.spi.OutingAvailableTimePort
import team.aliens.dms.domain.outing.spi.vo.OutingAvailableTimeVO
import team.aliens.dms.persistence.outing.entity.QOutingAvailableTimeJpaEntity.outingAvailableTimeJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingAvailableTimeMapper
import team.aliens.dms.persistence.outing.repository.OutingAvailableTimeJpaRepository
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingAvailableTimeVO
import java.time.DayOfWeek

@Component
class OutingAvailableTimePersistenceAdapter(
    private val outingAvailableTimeRepository: OutingAvailableTimeJpaRepository,
    private val outingAvailableTimeMapper: OutingAvailableTimeMapper,
    private val queryFactory: JPAQueryFactory
) : OutingAvailableTimePort {

    override fun queryOutingAvailableTimeByDayOfWeek(dayOfWeek: DayOfWeek) =
        outingAvailableTimeMapper.toDomain(
            outingAvailableTimeRepository.findByDayOfWeek(dayOfWeek)
        )

    override fun queryOutingAvailableTimesByDayOfWeek(dayOfWeek: DayOfWeek): List<OutingAvailableTimeVO> {
        return queryFactory
            .select(
                QQueryOutingAvailableTimeVO(
                    outingAvailableTimeJpaEntity.id,
                    outingAvailableTimeJpaEntity.outingTime,
                    outingAvailableTimeJpaEntity.arrivalTime,
                    outingAvailableTimeJpaEntity.enabled,
                    outingAvailableTimeJpaEntity.dayOfWeek
                )
            )
            .from(outingAvailableTimeJpaEntity)
            .where(
                outingAvailableTimeJpaEntity.dayOfWeek.eq(dayOfWeek)
            )
            .fetch()
    }
}
