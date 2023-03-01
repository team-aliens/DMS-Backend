package team.aliens.dms.persistence.student.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.entity.VerifiedStudentJpaEntity

@Component
class VerifiedStudentMapper : GenericMapper<VerifiedStudent, VerifiedStudentJpaEntity> {

    override fun toDomain(entity: VerifiedStudentJpaEntity?): VerifiedStudent? {
        return entity?.let {
            VerifiedStudent(
                id = entity.id!!,
                schoolName = entity.schoolName,
                name = entity.name,
                roomNumber = entity.roomNumber,
                roomLocation = entity.roomLocation,
                gcn = entity.gcn,
                sex = entity.sex
            )
        }
    }

    override fun toEntity(domain: VerifiedStudent): VerifiedStudentJpaEntity {
        return VerifiedStudentJpaEntity(
            id = domain.id,
            schoolName = domain.schoolName,
            name = domain.name,
            roomNumber = domain.roomNumber,
            roomLocation = domain.roomLocation,
            gcn = domain.gcn,
            sex = domain.sex
        )
    }
}
