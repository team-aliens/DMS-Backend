package team.aliens.dms.persistence.remain.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainRecord
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.remain.entity.RemainRecordJpaEntity
import team.aliens.dms.persistence.remain.repository.RemainOptionJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class RemainRecordMapper(
    private val studentRepository: StudentJpaRepository,
    private val remainOptionRepository: RemainOptionJpaRepository
) : GenericMapper<RemainRecord, RemainRecordJpaEntity> {

    override fun toDomain(entity: RemainRecordJpaEntity?): RemainRecord? {
        return entity?.let {
            RemainRecord(
                id = it.id,
                remainOptionId = it.remainOption!!.id!!,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: RemainRecord): RemainRecordJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.id)
        val remainOption = remainOptionRepository.findByIdOrNull(domain.remainOptionId)

        return RemainRecordJpaEntity(
            id = domain.id,
            student = student,
            remainOption = remainOption,
            createdAt = domain.createdAt
        )
    }
}