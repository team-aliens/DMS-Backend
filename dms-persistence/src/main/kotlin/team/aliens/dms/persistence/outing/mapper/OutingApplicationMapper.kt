package team.aliens.dms.persistence.outing.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.outing.entity.OutingApplicationJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId
import team.aliens.dms.persistence.outing.repository.OutingTypeJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class OutingApplicationMapper(
    private val outingTypeRepository: OutingTypeJpaRepository,
    private val studentRepository: StudentJpaRepository,
) : GenericMapper<OutingApplication, OutingApplicationJpaEntity> {

    override fun toDomain(entity: OutingApplicationJpaEntity?): OutingApplication? {
        return entity?.let {
            OutingApplication(
                id = it.id!!,
                studentId = it.student!!.id!!,
                createdAt = it.createdAt,
                outAt = it.outAt,
                outingTime = it.outingTime,
                arrivalTime = it.arrivalTime,
                status = it.status,
                reason = it.reason,
                destination = it.destination,
                outingTypeTitle = it.outingType!!.id.title,
                schoolId = it.outingType!!.id.schoolId
            )
        }
    }

    override fun toEntity(domain: OutingApplication): OutingApplicationJpaEntity {
        val id = OutingTypeJpaEntityId(
            title = domain.outingTypeTitle,
            schoolId = domain.schoolId
        )
        val outingType = outingTypeRepository.findOutingTypeJpaEntityById(id)
        val student = studentRepository.findByIdOrNull(domain.studentId)

        return OutingApplicationJpaEntity(
            id = domain.id,
            student = student,
            createdAt = domain.createdAt,
            outAt = domain.outAt,
            outingTime = domain.outingTime,
            arrivalTime = domain.arrivalTime,
            status = domain.status,
            reason = domain.reason,
            destination = domain.destination,
            outingType = outingType
        )
    }
}
