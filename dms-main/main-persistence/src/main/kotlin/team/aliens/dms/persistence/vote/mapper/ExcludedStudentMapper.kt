package team.aliens.dms.persistence.vote.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.vote.entity.ExcludedStudentJpaEntity

@Component
class ExcludedStudentMapper(
    val studentJpaRepository: StudentJpaRepository,
    val schoolJpaRepository: SchoolJpaRepository,
) : GenericMapper<ExcludedStudent, ExcludedStudentJpaEntity> {
    override fun toDomain(entity: ExcludedStudentJpaEntity?): ExcludedStudent? {
        return entity?.let {
            ExcludedStudent(
                studentId = it.student!!.id!!,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: ExcludedStudent): ExcludedStudentJpaEntity {
        val student = studentJpaRepository.findByIdOrNull(domain.studentId)
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return ExcludedStudentJpaEntity(
            student = student!!,
            school = school!!
        )
    }
}
