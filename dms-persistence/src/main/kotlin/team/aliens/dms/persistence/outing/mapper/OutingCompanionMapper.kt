package team.aliens.dms.persistence.outing.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntity
import team.aliens.dms.persistence.outing.entity.OutingCompanionJpaEntityId
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository

@Component
class OutingCompanionMapper(
    private val outingApplicationJpaRepository: OutingApplicationJpaRepository,
    private val studentRepository: StudentJpaRepository
) : GenericMapper<OutingCompanion, OutingCompanionJpaEntity> {

    override fun toDomain(entity: OutingCompanionJpaEntity?): OutingCompanion? {
        return entity?.let {
            OutingCompanion(
                outingApplicationId = it.outingApplication!!.id!!,
                studentId = it.student!!.id!!
            )
        }
    }

    override fun toEntity(domain: OutingCompanion): OutingCompanionJpaEntity {
        val id = OutingCompanionJpaEntityId(
            outingApplicationId = domain.outingApplicationId,
            studentId = domain.studentId
        )
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val outingApplication = outingApplicationJpaRepository.findByIdOrNull(domain.outingApplicationId)

        return OutingCompanionJpaEntity(
            id = id,
            outingApplication = outingApplication,
            student = student
        )
    }
}
