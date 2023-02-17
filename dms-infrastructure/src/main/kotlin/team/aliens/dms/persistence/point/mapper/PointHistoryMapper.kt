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
                studentGcn = it.studentGcn,
                bonusTotal = it.bonusTotal,
                minusTotal = it.minusTotal,
                isCancel = it.isCancel,
                pointName = it.pointName,
                pointScore = it.pointScore,
                pointType = it.pointType,
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
            studentGcn = domain.studentGcn,
            bonusTotal = domain.bonusTotal,
            minusTotal = domain.minusTotal,
            isCancel = domain.isCancel,
            pointName = domain.pointName,
            pointScore = domain.pointScore,
            pointType = domain.pointType,
            school = school,
            createdAt = domain.createdAt
        )
    }
}