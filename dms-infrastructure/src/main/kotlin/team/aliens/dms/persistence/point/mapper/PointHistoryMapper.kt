package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PointHistoryEntity
import team.aliens.dms.persistence.point.repository.PointOptionRepository
import team.aliens.dms.persistence.student.entity.StudentEntity
import team.aliens.dms.persistence.student.repository.StudentRepository

@Component
class PointHistoryMapper(
    private val pointOptionRepository: PointOptionRepository,
    private val studentRepository: StudentRepository
) : GenericMapper<PointHistory, PointHistoryEntity> {

    override fun toDomain(e: PointHistoryEntity): PointHistory {
        val pointOption = e.pointOptionEntity?.let {
            pointOptionRepository.findByIdOrNull(it.id)
        } ?: throw RuntimeException()

        val student = e.studentEntity?.let {
            studentRepository.findByIdOrNull(it.studentId)
        } ?: throw RuntimeException()

        return PointHistory(
            id = e.id,
            pointOptionId = pointOption.id,
            studentId = student.studentId,
            createdAt = e.createdAt
        )
    }

    override fun toEntity(d: PointHistory): PointHistoryEntity {
        val pointOption = pointOptionRepository.findByIdOrNull(d.pointOptionId) ?: throw RuntimeException()
        val student = studentRepository.findByIdOrNull(d.studentId) ?: throw RuntimeException()

        return PointHistoryEntity(
            id = d.id,
            pointOptionEntity = pointOption,
            studentEntity = student,
            createdAt = d.createdAt
        )
    }
}