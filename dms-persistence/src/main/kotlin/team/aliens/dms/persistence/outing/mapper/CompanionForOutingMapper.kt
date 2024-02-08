package team.aliens.dms.persistence.outing.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.CompanionForOuting
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.outing.entity.CompanionForOutingJpaEntity
import team.aliens.dms.persistence.outing.entity.CompanionForOutingJpaEntityId
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class CompanionForOutingMapper(
    private val outingApplicationJpaRepository: OutingApplicationJpaRepository,
    private val studentRepository: StudentJpaRepository
) : GenericMapper<CompanionForOuting, CompanionForOutingJpaEntity> {

    override fun toDomain(entity: CompanionForOutingJpaEntity?): CompanionForOuting? {
        return entity?.let {
            CompanionForOuting(
                outingApplicationId = it.outingApplication!!.id!!,
                studentId = it.student!!.id!!
            )
        }
    }

    override fun toEntity(domain: CompanionForOuting): CompanionForOutingJpaEntity {
        val id = CompanionForOutingJpaEntityId(
            outingApplicationId = domain.outingApplicationId,
            studentId = domain.studentId
        )
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val outingApplication = outingApplicationJpaRepository.findByIdOrNull(domain.outingApplicationId)

        return CompanionForOutingJpaEntity(
            id = id,
            outingApplication = outingApplication,
            student = student
        )
    }
}
