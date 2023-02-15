package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointHistoryJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class PointHistoryMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<PointHistory, PointHistoryJpaEntity> {

    override fun toDomain(entity: PointHistoryJpaEntity?): PointHistory? {
        return entity?.let {
            PointHistory(
                id = it.id!!,
                studentName = it.studentName,
                gcn = it.gcn,
                bonusTotal = it.bonusTotal,
                minusTotal = it.minusTotal,
                isCancel = it.isCancel,
                name = it.name,
                score = it.score,
                type = it.type,
                schoolId = it.school!!.id!!,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: PointHistory): PointHistoryJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return PointHistoryJpaEntity(
            id = domain.id,
            studentName = domain.studentName,
            gcn = domain.gcn,
            bonusTotal = domain.bonusTotal,
            minusTotal = domain.minusTotal,
            isCancel = domain.isCancel,
            name = domain.name,
            score = domain.score,
            type = domain.type,
            school = school,
            createdAt = domain.createdAt
        )
    }
}