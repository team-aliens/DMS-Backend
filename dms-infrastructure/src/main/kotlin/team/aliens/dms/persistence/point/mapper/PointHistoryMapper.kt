package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointHistoryJpaEntity
import team.aliens.dms.persistence.point.repository.PointOptionRepository
import team.aliens.dms.persistence.student.repository.StudentRepository

@Component
class PointHistoryMapper(
    private val pointOptionRepository: PointOptionRepository,
    private val studentRepository: StudentRepository
) : GenericMapper<PointHistory, PointHistoryJpaEntity> {

    override fun toDomain(e: PointHistoryJpaEntity): PointHistory {
        val pointOption = e.pointOptionJpaEntity?.let {
            pointOptionRepository.findByIdOrNull(it.id)
        } ?: throw PointOptionNotFoundException

        val student = e.studentJpaEntity?.let {
            studentRepository.findByIdOrNull(it.studentId)
        } ?: throw StudentNotFoundException

        return PointHistory(
            id = e.id,
            pointOptionId = pointOption.id,
            studentId = student.studentId,
            createdAt = e.createdAt
        )
    }

    override fun toEntity(d: PointHistory): PointHistoryJpaEntity {
        val pointOption = pointOptionRepository.findByIdOrNull(d.pointOptionId) ?: throw PointOptionNotFoundException
        val student = studentRepository.findByIdOrNull(d.studentId) ?: throw StudentNotFoundException

        return PointHistoryJpaEntity(
            id = d.id,
            pointOptionJpaEntity = pointOption,
            studentJpaEntity = student,
            createdAt = d.createdAt
        )
    }
}