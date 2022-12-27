package team.aliens.dms.persistence.student

import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.spi.VerifiedStudentPort
import team.aliens.dms.persistence.student.mapper.VerifiedStudentMapper
import team.aliens.dms.persistence.student.repository.VerifiedStudentJpaRepository

@Component
class VerifiedStudentPersistenceAdapter(
    private val verifiedStudentMapper: VerifiedStudentMapper,
    private val verifiedStudentRepository: VerifiedStudentJpaRepository
) : VerifiedStudentPort {

    override fun queryVerifiedStudentByGcnAndSchoolName(gcn: String, schoolName: String) = verifiedStudentMapper.toDomain(
        verifiedStudentRepository.findByGcnAndSchoolName(
            gcn = gcn,
            schoolName = schoolName
        )
    )
}