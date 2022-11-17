package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntity
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntityId

@Component
class VerifiedStudentMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<VerifiedStudent, VerifiedStudentJpaEntity> {

    override fun toDomain(entity: VerifiedStudentJpaEntity?): VerifiedStudent? {
        return entity?.let {
            VerifiedStudent(
                gcn = entity.id.gcn,
                schoolId = entity.id.schoolId,
                name = entity.name,
                roomNumber = entity.roomNumber
            )
        }
    }

    override fun toEntity(domain: VerifiedStudent): VerifiedStudentJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return VerifiedStudentJpaEntity(
            id = VerifiedStudentJpaEntityId(
                gcn = domain.gcn,
                schoolId = domain.schoolId
            ),
            school = school,
            name = domain.name,
            roomNumber = domain.roomNumber
        )
    }
}