package team.aliens.dms.persistence.remain

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.RemainStatusPort
import team.aliens.dms.persistence.remain.entity.QRemainOptionJpaEntity.remainOptionJpaEntity
import team.aliens.dms.persistence.remain.entity.QRemainStatusJpaEntity.remainStatusJpaEntity
import team.aliens.dms.persistence.remain.mapper.RemainStatusMapper
import team.aliens.dms.persistence.remain.repository.RemainStatusJpaRepository
import team.aliens.dms.persistence.remain.repository.vo.QRemainStatusInfoVO
import java.util.UUID

@Component
class RemainStatusPersistenceAdapter(
    private val remainStatusRepository: RemainStatusJpaRepository,
    private val remainStatusMapper: RemainStatusMapper,
    private val queryFactory: JPAQueryFactory
) : RemainStatusPort {

    override fun queryByStudentIdIn(studentIds: List<UUID>): List<RemainStatusInfo> {
        return queryFactory
            .select(
                QRemainStatusInfoVO(
                    remainStatusJpaEntity.student.id,
                    remainOptionJpaEntity.title
                )
            )
            .from(remainStatusJpaEntity)
            .join(remainStatusJpaEntity.remainOption, remainOptionJpaEntity)
            .where(
                remainStatusJpaEntity.student.id.`in`(studentIds)
            )
            .fetch()
            .map {
                RemainStatusInfo(
                    studentId = it.studentId,
                    optionName = it.optionName
                )
            }
    }

    override fun deleteRemainStatusByRemainOptionId(remainOptionId: UUID) {
        remainStatusRepository.deleteByRemainOptionId(remainOptionId)
    }

    override fun saveRemainStatus(remainStatus: RemainStatus) = remainStatusMapper.toDomain(
        remainStatusRepository.save(
            remainStatusMapper.toEntity(remainStatus)
        )
    )!!
}