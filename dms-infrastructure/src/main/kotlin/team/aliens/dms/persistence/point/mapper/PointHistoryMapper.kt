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
    private val pointOptionRepository: PointOptionJpaRepository,
    private val studentRepository: StudentJpaRepository
) : GenericMapper<PointHistory, PointHistoryJpaEntity> {

    override fun toDomain(entity: PointHistoryJpaEntity?): PointHistory? {
        return entity?.let {
            PointHistory(
                id = it.id,
                pointOptionId = it.pointOption!!.id,
                studentId = it.student!!.id,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: PointHistory): PointHistoryJpaEntity {
        val pointOption = pointOptionRepository.findByIdOrNull(domain.pointOptionId)
        val student = studentRepository.findByIdOrNull(domain.studentId)

        return PointHistoryJpaEntity(
            id = domain.id,
            pointOption = pointOption,
            student = student,
            createdAt = domain.createdAt!!
        )
    }
}