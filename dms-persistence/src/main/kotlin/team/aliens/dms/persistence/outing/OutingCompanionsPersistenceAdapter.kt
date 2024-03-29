package team.aliens.dms.persistence.outing

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.spi.OutingCompanionPort
import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import team.aliens.dms.persistence.outing.entity.QOutingCompanionJpaEntity.outingCompanionJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingCompanionMapper
import team.aliens.dms.persistence.outing.repository.OutingCompanionJpaRepository
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingCompanionDetailsVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import java.util.UUID

@Component
class OutingCompanionsPersistenceAdapter(
    private val outingCompanionRepository: OutingCompanionJpaRepository,
    private val outingCompanionMapper: OutingCompanionMapper,
    private val queryFactory: JPAQueryFactory
) : OutingCompanionPort {

    override fun queryOutingCompanionsById(outingApplicationId: UUID): List<OutingCompanionDetailsVO> {
        return queryFactory
            .select(
                QQueryOutingCompanionDetailsVO(
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    studentJpaEntity.room.number
                )
            )
            .from(outingCompanionJpaEntity)
            .join(outingCompanionJpaEntity.student, studentJpaEntity)
            .where(
                outingCompanionJpaEntity.outingApplication.id.eq(outingApplicationId)
            )
            .fetch()
    }

    override fun saveAllOutingCompanions(outingCompanions: List<OutingCompanion>) {
        outingCompanionRepository.saveAll(
            outingCompanions.map {
                outingCompanionMapper.toEntity(it)
            }
        )
    }
}
