package team.aliens.dms.persistence.remain

import org.springframework.data.repository.findByIdOrNull
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.spi.RemainStatusPort
import team.aliens.dms.persistence.remain.mapper.RemainStatusMapper
import team.aliens.dms.persistence.remain.repository.RemainStatusJpaRepository
import java.util.UUID

@Component
class RemainStatusPersistenceAdapter(
    private val remainStatusRepository: RemainStatusJpaRepository,
    private val remainStatusMapper: RemainStatusMapper,
    private val queryFactory: JPAQueryFactory,
) : RemainStatusPort {

    override fun deleteRemainStatusByRemainOptionId(remainOptionId: UUID) {
        remainStatusRepository.deleteByRemainOptionId(remainOptionId)
    }
    
    override fun queryRemainStatusById(userId: UUID) = remainStatusMapper.toDomain(
        remainStatusRepository.findByIdOrNull(userId)
    )
}