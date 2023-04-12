package team.aliens.dms.persistence.remain.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.remain.entity.RemainStatusJpaEntity
import team.aliens.dms.persistence.remain.repository.RemainOptionJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class RemainStatusMapper(
    private val studentRepository: StudentJpaRepository,
    private val remainOptionRepository: RemainOptionJpaRepository
) : GenericMapper<RemainStatus, RemainStatusJpaEntity> {

    override fun toDomain(entity: RemainStatusJpaEntity?): RemainStatus? {
        return entity?.let {
            RemainStatus(
                id = it.id,
                remainOptionId = it.remainOption!!.id!!,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: RemainStatus): RemainStatusJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.id)
        val remainOption = remainOptionRepository.findByIdOrNull(domain.remainOptionId)

        return RemainStatusJpaEntity(
            id = domain.id,
            student = student,
            remainOption = remainOption,
            createdAt = domain.createdAt
        )
    }
}
