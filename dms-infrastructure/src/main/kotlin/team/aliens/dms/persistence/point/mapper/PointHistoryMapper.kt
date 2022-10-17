package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointHistoryJpaEntity
import team.aliens.dms.persistence.point.repository.PointOptionJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class PointHistoryMapper(
    private val pointOptionJpaRepository: PointOptionJpaRepository,
    private val studentJpaRepository: StudentJpaRepository
) : GenericMapper<PointHistory, PointHistoryJpaEntity> {

    override fun toDomain(entity: PointHistoryJpaEntity?): PointHistory? {
        return PointHistory(
            id = entity!!.id,
            pointOptionId = entity.pointOption!!.id,
            studentId = entity.student!!.studentId,
            createdAt = entity.createdAt
        )
    }

    override fun toEntity(domain: PointHistory): PointHistoryJpaEntity {
        val pointOption = pointOptionJpaRepository.findByIdOrNull(domain.pointOptionId)!!
        val student = studentJpaRepository.findByIdOrNull(domain.studentId)!!

        return PointHistoryJpaEntity(
            id = domain.id!!,
            pointOption = pointOption,
            student = student,
            createdAt = domain.createdAt!!
        )
    }
}