package team.aliens.dms.persistence.outing

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.spi.OutingAvailableTimePort
import team.aliens.dms.persistence.outing.entity.QOutingAvailableTimeJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingAvailableTimeMapper
import team.aliens.dms.persistence.outing.repository.OutingAvailableTimeJpaRepository
import java.time.DayOfWeek
import java.util.UUID

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

    override fun queryOutingAvailableTimesByDayOfWeek(dayOfWeek: DayOfWeek): List<OutingAvailableTime> {
        val qOutingAvailableTimeJpaEntity = QOutingAvailableTimeJpaEntity.outingAvailableTimeJpaEntity
        val entities = queryFactory
            .selectFrom(qOutingAvailableTimeJpaEntity)
            .where(qOutingAvailableTimeJpaEntity.dayOfWeek.eq(dayOfWeek))
            .fetch()

        return entities.mapNotNull { outingAvailableTimeMapper.toDomain(it) }
    }

    override fun saveOutingAvailableTime(outingAvailableTime: OutingAvailableTime): OutingAvailableTime =
        outingAvailableTimeMapper.toDomain(
            outingAvailableTimeRepository.save(
                outingAvailableTimeMapper.toEntity(outingAvailableTime)
            )
        )!!

    override fun queryOutingAvailableTimeById(outingAvailableTimeId: UUID) = outingAvailableTimeMapper.toDomain(
        outingAvailableTimeRepository.findByIdOrNull(outingAvailableTimeId)
    )

    override fun deleteOutingAvailableTime(outingAvailableTime: OutingAvailableTime) {
        outingAvailableTimeRepository.delete(
            outingAvailableTimeMapper.toEntity(outingAvailableTime)
        )
    }
}
