package team.aliens.dms.persistence.tag.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.tag.entity.StudentTagId
import team.aliens.dms.persistence.tag.entity.StudentTagJpaEntity
import team.aliens.dms.persistence.tag.repository.TagJpaRepository

@Component
class StudentTagMapper(
    private val studentRepository: StudentJpaRepository,
    private val tagRepository: TagJpaRepository
) : GenericMapper<StudentTag, StudentTagJpaEntity> {

    override fun toDomain(entity: StudentTagJpaEntity?): StudentTag? {
        return entity?.let {
            StudentTag(
                studentId = it.student!!.id!!,
                tagId = it.tag!!.id!!,
                createdAt = it.createdAt
            )
        }
    }

    override fun toEntity(domain: StudentTag): StudentTagJpaEntity {
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val tag = tagRepository.findByIdOrNull(domain.tagId)
        val id = StudentTagId(
            studentId = domain.studentId,
            tagId = domain.tagId
        )

        return StudentTagJpaEntity(
            id = id,
            student = student,
            tag = tag
        )
    }
}
