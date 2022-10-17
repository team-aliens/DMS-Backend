package team.aliens.dms.persistence.point.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.student.exception.StudentNotFoundException
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
        val pointOption = entity?.pointOption?.let {
            pointOptionJpaRepository.findByIdOrNull(it.id)
        } ?: throw PointOptionNotFoundException

        val student = entity.student?.let {
            studentJpaRepository.findByIdOrNull(it.studentId)
        } ?: throw StudentNotFoundException

        return entity.let {
            PointHistory(
                id = it.id,
                pointOptionId = pointOption.id,
                studentId = student.studentId,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: PointHistory): PointHistoryJpaEntity {
        val pointOption = pointOptionJpaRepository.findByIdOrNull(domain.pointOptionId) ?: throw PointOptionNotFoundException
        val student = studentJpaRepository.findByIdOrNull(domain.studentId) ?: throw StudentNotFoundException


        return PointHistoryJpaEntity(
            id = domain.id ?: throw PointHistoryNotFoundException,
            pointOption = pointOption,
            student = student,
            createdAt = domain.createdAt ?: throw PointHistoryNotFoundException
        )
    }
}