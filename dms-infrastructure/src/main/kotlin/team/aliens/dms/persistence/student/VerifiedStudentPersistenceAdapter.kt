package team.aliens.dms.persistence.student

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.spi.VerifiedStudentPort
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntityId
import team.aliens.dms.persistence.student.mapper.VerifiedStudentMapper
import team.aliens.dms.persistence.student.repository.VerifiedStudentJpaRepository

@Component
class VerifiedStudentPersistenceAdapter(
    private val verifiedStudentMapper: VerifiedStudentMapper,
    private val verifiedStudentRepository: VerifiedStudentJpaRepository
) : VerifiedStudentPort {

    override fun queryVerifiedStudentByGcnAndSchoolId(gcn: String, schoolId: UUID) = verifiedStudentMapper.toDomain(
        verifiedStudentRepository.findByIdOrNull(
            VerifiedStudentJpaEntityId(
                gcn = gcn,
                schoolId = schoolId
            )
        )
    )
}