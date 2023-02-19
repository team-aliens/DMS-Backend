package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.spi.PointOptionPort
import team.aliens.dms.persistence.point.mapper.PointOptionMapper
import team.aliens.dms.persistence.point.repository.PointOptionJpaRepository
import java.util.*

@Component
class PointOptionPersistenceAdapter(
    private val queryFactory: JPAQueryFactory,
    private val pointOptionMapper: PointOptionMapper,
    private val pointOptionJpaRepository: PointOptionJpaRepository
) : PointOptionPort {

    override fun queryPointOptionById(pointOptionId: UUID) = pointOptionMapper.toDomain(
        pointOptionJpaRepository.findByIdOrNull(pointOptionId)
    )
}