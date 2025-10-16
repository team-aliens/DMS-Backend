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
                outingDate = it.outingDate,
                outingTime = it.outingTime,
                arrivalTime = it.arrivalTime,
                isApproved = it.isApproved,
                isReturned = it.isReturned,
                reason = it.reason,
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
        val outingType = outingTypeRepository.findByIdOrNull(id)
        val student = studentRepository.findByIdOrNull(domain.studentId)

        return OutingApplicationJpaEntity(
            id = domain.id,
            student = student,
            createdAt = domain.createdAt,
            outingDate = domain.outingDate,
            outingTime = domain.outingTime,
            arrivalTime = domain.arrivalTime,
            isApproved = domain.isApproved,
            isReturned = domain.isReturned,
            reason = domain.reason,
            outingType = outingType
        )
    }
}
