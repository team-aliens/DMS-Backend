package team.aliens.dms.persistence.student.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.entity.VerifiedStudentEntity

@Component
class VerifiedStudentMapper : GenericMapper<VerifiedStudent, VerifiedStudentEntity> {

    override fun toDomain(entity: VerifiedStudentEntity?): VerifiedStudent? {
        return entity?.let {
            VerifiedStudent(
                id = entity.id,
                schoolName = entity.schoolName,
                name = entity.name,
                roomNumber = entity.roomNumber,
                gcn = entity.gcn
            )
        }
    }

    override fun toEntity(domain: VerifiedStudent): VerifiedStudentEntity {
        return VerifiedStudentEntity(
            id = domain.id,
            schoolName = domain.schoolName,
            name = domain.name,
            roomNumber = domain.roomNumber,
            gcn = domain.gcn
        )
    }
}