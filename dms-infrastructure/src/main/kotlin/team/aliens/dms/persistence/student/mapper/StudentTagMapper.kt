package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import team.aliens.dms.domain.student.model.StudentTag
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.entity.StudentTagJpaEntity
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.student.repository.StudentTagJpaRepository
import team.aliens.dms.persistence.student.repository.TagJpaRepository

class StudentTagMapper(
    private val studentRepository: StudentJpaRepository,
    private val tagRepository: TagJpaRepository,
    private val studentTagRepository: StudentTagJpaRepository
) : GenericMapper<StudentTag, StudentTagJpaEntity> {

    override fun toDomain(entity: StudentTagJpaEntity?): StudentTag? {
        return entity?.let {
            StudentTag(
                studentId = entity.student!!.id,
                tagId = entity.tag!!.id!!
            )
        }
    }

    override fun toEntity(domain: StudentTag): StudentTagJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val tag = tagRepository.findByIdOrNull(domain.tagId)
        val id = StudentTagJpaEntity.Id(
            studentId = student!!.id,
            tagId = tag!!.id!!
        )

        return StudentTagJpaEntity(
            id = id,
            student = student,
            tag = tag
        )
    }
}