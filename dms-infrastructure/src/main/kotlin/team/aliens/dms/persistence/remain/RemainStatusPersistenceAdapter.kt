package team.aliens.dms.persistence.remain

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.RemainStatusPort
import team.aliens.dms.persistence.remain.mapper.RemainStatusMapper
import team.aliens.dms.persistence.remain.repository.RemainStatusJpaRepository

@Component
class RemainStatusPersistenceAdapter(
    private val remainStatusRepository: RemainStatusJpaRepository,
    private val remainStatusMapper: RemainStatusMapper,
    private val queryFactory: JPAQueryFactory,
) : RemainStatusPort {

    override fun saveRemainStatus(remainStatus: RemainStatus) = remainStatusMapper.toDomain(
        remainStatusRepository.save(
            remainStatusMapper.toEntity(remainStatus)
        )
    )!!
}